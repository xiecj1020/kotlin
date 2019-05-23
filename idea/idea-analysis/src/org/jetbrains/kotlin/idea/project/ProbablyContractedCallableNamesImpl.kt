/*
 * Copyright 2010-2018 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.idea.project

import com.intellij.openapi.project.Project
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiModificationTracker
import org.jetbrains.kotlin.idea.stubindex.KotlinProbablyContractedFunctionShortNameIndex
import org.jetbrains.kotlin.resolve.lazy.ProbablyContractedCallableNames

class ProbablyContractedCallableNamesImpl(project: Project) : ProbablyContractedCallableNames {
    private val functionNames = CachedValuesManager.getManager(project).createCachedValue(
        {
            val contractedFunctionShortNameIndex = KotlinProbablyContractedFunctionShortNameIndex.getInstance()

            println(
                "ProbablyContractedCallableNamesImpl: " +
                        contractedFunctionShortNameIndex.tracker.modificationCount + " " +
                        Thread.currentThread().id
            )

            CachedValueProvider.Result.create(
                contractedFunctionShortNameIndex.getAllKeys(project),
                contractedFunctionShortNameIndex.tracker
            )
        },
        false
    )

    override fun isProbablyContractedCallableName(name: String): Boolean = name in functionNames.value
}