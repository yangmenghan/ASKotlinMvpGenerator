package template;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import template.metadata.MVPMetaData;
import template.metadata.MVPMetaDataFactory;
import template.utils.CaseUtil;
import template.utils.FileUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author yyx
 * @date 2017/11/6
 */
public class AndroidMvpAction extends AnAction {
    Project project;
    VirtualFile selectGroup;
    MVPMetaData metaData;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getProject();
        selectGroup = DataKeys.VIRTUAL_FILE.getData(e.getDataContext());
        String className =
                Messages.showInputDialog(project, "Please enter view name", "NewMvpGroup", Messages.getQuestionIcon());
        if (className == null || className.equals("")) {
            System.out.print("No name provided");
            return;
        }
        metaData = MVPMetaDataFactory.getMetaData(className, getPrefix(className), selectGroup, project);
        createMvp();
        project.getBaseDir().refresh(false, true);
    }

    /**
     * Create MVP
     */
    private void createMvp() {
        String layout = FileUtil.readFile("layout.txt");

        generateFile("Contract", metaData.path);
        generateFile("Presenter", metaData.path);
        generateFile("Module", metaData.path + "/di/");
        if (metaData.useFragment) {
            generateFile("Fragment", metaData.path);
            FileUtil.writeToFile(layout, metaData.layoutPath, "fragment_" + CaseUtil.camelToSnakeCase(metaData.prefix) + ".xml");
        } else {
            generateFile("Activity", metaData.path);
            FileUtil.writeToFile(layout, metaData.layoutPath, "activity_" + CaseUtil.camelToSnakeCase(metaData.prefix) + ".xml");
        }
    }

    private void generateFile(String filename, String path) {
        String viewTypeString = metaData.useFragment ? "Fragment" : "Activity";
        String content = FileUtil.readFile(filename + ".txt")
                .replace("&Contract&", metaData.prefix + "Contract")
                .replace("&Presenter&", metaData.prefix + "Presenter")
                .replace("&Activity&", metaData.prefix + "Activity")
                .replace("&Fragment&", metaData.prefix + "Fragment")
                .replace("&Module&", metaData.prefix + "Module")
                .replace("&View&", metaData.prefix + "View")
                .replace("&Binding&", viewTypeString + metaData.prefix + "Binding")
                .replace("&ViewClass&", metaData.prefix + viewTypeString)
                .replace("&pack&", metaData.rootPackage)
                .replace("&package&", metaData.packageName);

        FileUtil.writeToFile(content, path, metaData.prefix + filename + ".kt");
    }

    private String getPrefix(String prefix) {
        if (prefix.endsWith("Fragment")
                || prefix.endsWith("fragment")
                || prefix.endsWith("Activity")
                || prefix.endsWith("activity")) {
            prefix = prefix.substring(0, prefix.length() - 8);
        }
        return prefix;
    }
}
