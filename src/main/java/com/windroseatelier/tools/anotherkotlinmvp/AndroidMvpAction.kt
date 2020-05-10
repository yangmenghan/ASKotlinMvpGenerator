package com.windroseatelier.tools.anotherkotlinmvp

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.guessProjectDir
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import com.windroseatelier.tools.anotherkotlinmvp.metadata.MVPMetaData
import com.windroseatelier.tools.anotherkotlinmvp.metadata.MVPMetaDataFactory
import com.windroseatelier.tools.anotherkotlinmvp.utils.CaseUtil
import com.windroseatelier.tools.anotherkotlinmvp.utils.FileUtil

/**
 * @author yyx
 * @date 2017/11/6
 */
class AndroidMvpAction : AnAction() {
    private lateinit var project: Project
    private lateinit var selectGroup: VirtualFile
    private lateinit var metaData: MVPMetaData

    override fun actionPerformed(e: AnActionEvent) {
        project = e.getData(CommonDataKeys.PROJECT)
            ?: return Messages.showWarningDialog(project, "Should be in project", "Project not found")
        selectGroup = e.getData(CommonDataKeys.VIRTUAL_FILE)
            ?: return Messages.showWarningDialog(project, "No group selected", "Group not found")
        val className =
            Messages.showInputDialog(project, "Please enter view name", "NewMvpGroup", Messages.getQuestionIcon())
        if (className.isNullOrEmpty()) {
            print("No name provided")
            return
        }
        metaData = MVPMetaDataFactory.getMetaData(className, getPrefix(className), selectGroup, project)
        createMvp()
        project.guessProjectDir()?.refresh(false, true)
    }

    /**
     * Create MVP
     */
    private fun createMvp() {
        val layout: String = FileUtil.readFile("layout.txt")
        generateFile("Contract", metaData.path)
        generateFile("Presenter", metaData.path)
        generateFile("Module", metaData.path + "/di/")
        if (metaData.useFragment) {
            generateFile("Fragment", metaData.path)
            FileUtil.writeToFile(
                layout,
                metaData.layoutPath,
                "fragment_" + CaseUtil.camelToSnakeCase(metaData.prefix) + ".xml"
            )
        } else {
            generateFile("Activity", metaData.path)
            FileUtil.writeToFile(
                layout,
                metaData.layoutPath,
                "activity_" + CaseUtil.camelToSnakeCase(metaData.prefix) + ".xml"
            )
        }
    }

    private fun generateFile(filename: String, path: String) {
        val viewTypeString = if (metaData.useFragment) "Fragment" else "Activity"
        val content: String = FileUtil.readFile("$filename.txt")
            .replace("&Contract&", metaData.prefix + "Contract")
            .replace("&Presenter&", metaData.prefix + "Presenter")
            .replace("&Activity&", metaData.prefix + "Activity")
            .replace("&Fragment&", metaData.prefix + "Fragment")
            .replace("&Module&", metaData.prefix + "Module")
            .replace("&View&", metaData.prefix + "View")
            .replace("&Binding&", viewTypeString + metaData.prefix + "Binding")
            .replace("&ViewClass&", metaData.prefix + viewTypeString)
            .replace("&pack&", metaData.rootPackage)
            .replace("&package&", metaData.packageName)
        FileUtil.writeToFile(content, path, metaData.prefix + filename + ".kt")
    }

    private fun getPrefix(prefix: String): String =
        if (prefix.endsWith("Fragment")
            || prefix.endsWith("fragment")
            || prefix.endsWith("Activity")
            || prefix.endsWith("activity")
        ) prefix.substring(0, prefix.length - 8) else prefix
}
