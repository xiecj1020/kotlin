/*
 * Copyright 2010-2019 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license
 * that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.core.script.dependencies

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiManager
import org.jetbrains.kotlin.idea.core.script.ScriptDependenciesManager
import org.jetbrains.kotlin.idea.highlighter.OutsidersPsiFileSupportUtils
import org.jetbrains.kotlin.idea.highlighter.OutsidersPsiFileSupportWrapper
import org.jetbrains.kotlin.psi.KtFile

class OutsiderFileDependenciesLoader(project: Project) : ScriptDependenciesLoader(project) {
    override fun isApplicable(file: KtFile): Boolean {
        return OutsidersPsiFileSupportWrapper.isOutsiderFile(file.virtualFile)
    }

    override fun loadDependencies(file: KtFile) {
        val fileOrigin = OutsidersPsiFileSupportUtils.getOutsiderFileOrigin(project, file.virtualFile) ?: return
        val ktFile = PsiManager.getInstance(project).findFile(fileOrigin) as? KtFile ?: return
        saveToCache(file, ScriptDependenciesManager.getInstance(project).getScriptDependencies(ktFile))
    }

    override fun shouldShowNotification(): Boolean = false
}