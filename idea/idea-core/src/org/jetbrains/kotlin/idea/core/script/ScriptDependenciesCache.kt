/*
 * Copyright 2010-2017 JetBrains s.r.o.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jetbrains.kotlin.idea.core.script

import com.intellij.codeInsight.daemon.DaemonCodeAnalyzer
import com.intellij.openapi.extensions.Extensions
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElementFinder
import com.intellij.psi.search.NonClasspathDirectoriesScope
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.util.containers.SLRUCache
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.jetbrains.kotlin.idea.core.util.EDT
import org.jetbrains.kotlin.psi.KtFile
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty0
import kotlin.reflect.jvm.isAccessible
import kotlin.script.experimental.dependencies.ScriptDependencies

class ScriptDependenciesCache(private val project: Project) {

    companion object {
        const val MAX_SCRIPTS_CACHED = 50
    }

    private val scriptDependenciesProvider = CachedValueProvider<SLRUCache<KtFile, ScriptDependencies?>> {
        CachedValueProvider.Result(
            object : SLRUCache<KtFile, ScriptDependencies?>(MAX_SCRIPTS_CACHED, MAX_SCRIPTS_CACHED) {
                override fun createValue(file: KtFile): ScriptDependencies = ScriptDependencies.Empty
            },
            ScriptDependenciesModificationTracker.getInstance(project)
        )
    }

    private val cache = CachedValuesManager.getManager(project).getCachedValue(
        project,
        scriptDependenciesProvider
    )

    private fun getScriptDependencies(file: KtFile): ScriptDependencies? {
        return cacheLock.write {
            cache.getIfCached(file)
        }
    }

    private fun getAllScriptDependencies(): List<Pair<KtFile, ScriptDependencies>> {
        return cache.entrySet().mapNotNull { it.takeIf { it.value != null }?.let { it.key to it.value!! } }
    }

    private val cacheLock = ReentrantReadWriteLock()

    operator fun get(virtualFile: KtFile): ScriptDependencies? = getScriptDependencies(virtualFile)

    val allScriptsClasspath by ClearableLazyValue(cacheLock) {
        val files = getAllScriptDependencies().flatMap { it.second.classpath }.distinct()
        ScriptDependenciesManager.toVfsRoots(files)
    }

    val allScriptsClasspathScope by ClearableLazyValue(cacheLock) {
        NonClasspathDirectoriesScope(allScriptsClasspath)
    }

    val allLibrarySources by ClearableLazyValue(cacheLock) {
        ScriptDependenciesManager.toVfsRoots(getAllScriptDependencies().flatMap { it.second.sources }.distinct())
    }

    val allLibrarySourcesScope by ClearableLazyValue(cacheLock) {
        NonClasspathDirectoriesScope(allLibrarySources)
    }

    private fun onChange(files: List<KtFile>) {
        this::allScriptsClasspath.clearValue()
        this::allScriptsClasspathScope.clearValue()
        this::allLibrarySources.clearValue()
        this::allLibrarySourcesScope.clearValue()

        val kotlinScriptDependenciesClassFinder =
            Extensions.getArea(project).getExtensionPoint(PsiElementFinder.EP_NAME).extensions
                .filterIsInstance<KotlinScriptDependenciesClassFinder>()
                .single()

        kotlinScriptDependenciesClassFinder.clearCache()
        updateHighlighting(files)
    }

    private fun updateHighlighting(files: List<KtFile>) {
        ScriptDependenciesModificationTracker.getInstance(project).incModificationCount()

        GlobalScope.launch(EDT(project)) {
            files.filter { it.isValid }.forEach {
                it.let { psiFile ->
                    DaemonCodeAnalyzer.getInstance(project).restart(psiFile)
                }
            }
        }
    }

    fun hasNotCachedRoots(scriptDependencies: ScriptDependencies): Boolean {
        return !allScriptsClasspath.containsAll(ScriptDependenciesManager.toVfsRoots(scriptDependencies.classpath)) ||
                !allLibrarySources.containsAll(ScriptDependenciesManager.toVfsRoots(scriptDependencies.sources))
    }

    fun clear() {
        val keys = cacheLock.read {
            val keys = getAllScriptDependencies().map { it.first }
            cacheLock.write {
                cache.clear()
            }
            keys
        }
        onChange(keys)
    }

    fun save(virtualFile: KtFile, new: ScriptDependencies): Boolean {
        val old = cacheLock.write {
            val old = getScriptDependencies(virtualFile)
            cache.put(virtualFile, new)
            old
        }
        val changed = new != old
        if (changed) {
            onChange(listOf(virtualFile))
        }

        return changed
    }

    fun delete(virtualFile: KtFile): Boolean {
        val changed = cacheLock.write {
            cache.remove(virtualFile)
        }
        if (changed) {
            onChange(listOf(virtualFile))
        }
        return changed
    }

}

private fun <R> KProperty0<R>.clearValue() {
    isAccessible = true
    (getDelegate() as ClearableLazyValue<*, *>).clear()
}

private class ClearableLazyValue<in R, out T : Any>(
    private val lock: ReentrantReadWriteLock,
    private val compute: () -> T
) : ReadOnlyProperty<R, T> {
    override fun getValue(thisRef: R, property: KProperty<*>): T {
        lock.write {
            if (value == null) {
                value = compute()
            }
            return value!!
        }
    }

    private var value: T? = null


    fun clear() {
        lock.write {
            value = null
        }
    }
}

