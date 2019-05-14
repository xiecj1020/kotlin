/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.scripting.definitions

import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import org.jetbrains.kotlin.psi.KtFile
import kotlin.script.experimental.dependencies.ScriptDependencies

interface ScriptDependenciesProvider {
    fun getScriptDependencies(file: KtFile): ScriptDependencies?

    companion object {
        fun getInstance(project: Project): ScriptDependenciesProvider? =
            ServiceManager.getService(project, ScriptDependenciesProvider::class.java)
    }
}
