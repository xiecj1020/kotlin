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
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.intellij.ui.EditorNotifications
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.scripting.resolve.ScriptReportSink
import kotlin.script.experimental.dependencies.ScriptReport

class IdeScriptReportSink(val project: Project) : ScriptReportSink {
    override fun attachReports(scriptFile: KtFile, reports: List<ScriptReport>) {
        // TODO: persist errors between launches?
        scriptFile.putUserData(Reports, reports)

        ApplicationManager.getApplication().invokeLater {
            if (scriptFile.isValid && !project.isDisposed) {
                DaemonCodeAnalyzer.getInstance(project).restart(scriptFile)
                EditorNotifications.getInstance(project).updateNotifications(scriptFile.virtualFile)
            }
        }
    }

    private object Reports : Key<List<ScriptReport>>("KOTLIN_SCRIPT_REPORTS")

    companion object {
        fun getInstance(project: Project): ScriptReportSink = ServiceManager.getService(project, ScriptReportSink::class.java)

        fun attach(file: KtFile, reports: List<ScriptReport>) {
            if (file.getUserData(Reports) != reports.takeIf { it.isNotEmpty() }) {
                getInstance(file.project).attachReports(file, reports)
            }
        }

        fun readReports(file: KtFile): List<ScriptReport> {
            return file.getUserData(Reports) ?: emptyList()
        }
    }
}