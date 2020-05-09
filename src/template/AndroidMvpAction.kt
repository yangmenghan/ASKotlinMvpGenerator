package template

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.DataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VirtualFile
import template.metadata.MVPMetaData
import template.metadata.MVPMetaDataFactory
import template.utils.CaseUtil
import template.utils.FileUtil
import java.util.regex.Matcher
import java.util.regex.Pattern

/**
 * @author yyx
 * @date 2017/11/6
 */
class AndroidMvpAction : AnAction() {
    var project: Project? = null
    var selectGroup: VirtualFile? = null
    var metaData: MVPMetaData? = null

    @Override
    fun actionPerformed(e: AnActionEvent) {
        project = e.getProject()
        selectGroup = DataKeys.VIRTUAL_FILE.getData(e.getDataContext())
        val className: String =
            Messages.showInputDialog(project, "Please enter view name", "NewMvpGroup", Messages.getQuestionIcon())
        if (className == null || className.equals("")) {
            System.out.print("No name provided")
            return
        }
        metaData = MVPMetaDataFactory.getMetaData(className, getPrefix(className), selectGroup, project)
        createMvp()
        project.getBaseDir().refresh(false, true)
    }

    /**
     * Create MVP
     */
    private fun createMvp() {
        val layout: String = FileUtil.readFile("layout.txt")
        generateFile("Contract", metaData.path)
        generateFile("Presenter", metaData.path)
        generateFile("Module", metaData.path.toString() + "/di/")
        if (metaData.useFragment) {
            generateFile("Fragment", metaData.path)
            FileUtil.writeToFile(
                layout,
                metaData.layoutPath,
                "fragment_" + CaseUtil.camelToSnakeCase(metaData.prefix).toString() + ".xml"
            )
        } else {
            generateFile("Activity", metaData.path)
            FileUtil.writeToFile(
                layout,
                metaData.layoutPath,
                "activity_" + CaseUtil.camelToSnakeCase(metaData.prefix).toString() + ".xml"
            )
        }
    }

    private fun generateFile(filename: String, path: String) {
        val viewTypeString = if (metaData.useFragment) "Fragment" else "Activity"
        val content: String = FileUtil.readFile("$filename.txt")
            .replace("&Contract&", metaData.prefix.toString() + "Contract")
            .replace("&Presenter&", metaData.prefix.toString() + "Presenter")
            .replace("&Activity&", metaData.prefix.toString() + "Activity")
            .replace("&Fragment&", metaData.prefix.toString() + "Fragment")
            .replace("&Module&", metaData.prefix.toString() + "Module")
            .replace("&View&", metaData.prefix.toString() + "View")
            .replace("&Binding&", viewTypeString + metaData.prefix.toString() + "Binding")
            .replace("&ViewClass&", metaData.prefix + viewTypeString)
            .replace("&pack&", metaData.rootPackage)
            .replace("&package&", metaData.packageName)
        FileUtil.writeToFile(content, path, metaData.prefix + filename + ".kt")
    }

    private fun getPrefix(prefix: String): String {
        var prefix = prefix
        if (prefix.endsWith("Fragment")
            || prefix.endsWith("fragment")
            || prefix.endsWith("Activity")
            || prefix.endsWith("activity")
        ) {
            prefix = prefix.substring(0, prefix.length() - 8)
        }
        return prefix
    }
}