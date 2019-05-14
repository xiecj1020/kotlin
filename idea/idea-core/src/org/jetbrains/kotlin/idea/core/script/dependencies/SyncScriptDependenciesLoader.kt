/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.core.script.dependencies

import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.idea.core.script.ScriptDependenciesUpdater
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.scripting.definitions.scriptDefinition

class SyncScriptDependenciesLoader internal constructor(project: Project) : ScriptDependenciesLoader(project) {
    override fun isApplicable(file: KtFile): Boolean {
        val scriptDefinition = file.scriptDefinition() ?: return false
        return !ScriptDependenciesUpdater.getInstance(project).isAsyncDependencyResolver(scriptDefinition)
    }

    override fun loadDependencies(file: KtFile) {
        val scriptDef = file.scriptDefinition() ?: return
        val result = contentLoader.loadContentsAndResolveDependencies(scriptDef, file)
        processResult(result, file, scriptDef)
    }

    override fun shouldShowNotification(): Boolean = false
}