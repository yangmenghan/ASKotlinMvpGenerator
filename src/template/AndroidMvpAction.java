package template;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import template.metadata.MVPMetaData;
import template.metadata.MVPMetaDataFactory;

import java.io.*;
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
        String layout = readFile("layout.txt");

        generateFile("Contract", metaData.path);
        generateFile("Presenter", metaData.path);
        generateFile("Module", metaData.path + "/di/");
        if (metaData.useFragment) {
            generateFile("Fragment", metaData.path);
            writetoFile(layout, metaData.layoutPath, "fragment_" + camelToSnakeCase(metaData.prefix) + ".xml");
        } else {
            generateFile("Activity", metaData.path);
            writetoFile(layout, metaData.layoutPath, "activity_" + camelToSnakeCase(metaData.prefix) + ".xml");
        }
    }

    private void generateFile(String filename, String path) {
        String viewTypeString = metaData.useFragment ? "Fragment" : "Activity";
        String content = readFile(filename + ".txt")
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

        writetoFile(content, path, metaData.prefix + filename + ".kt");
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

    public static String camelToSnakeCase(String line) {
        if (line == null || "".equals(line)) {
            return "";
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1));
        StringBuffer sb = new StringBuffer();
        Pattern pattern = Pattern.compile("[A-Z]([a-z\\d]+)?");
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String word = matcher.group();
            sb.append(word.toLowerCase());
            sb.append(matcher.end() == line.length() ? "" : "_");
        }
        return sb.toString();
    }

    private String readFile(String filename) {
        InputStream in = null;
        in = this.getClass().getResourceAsStream("code/" + filename);
        String content = "";
        try {
            content = new String(readStream(in));
        } catch (Exception e) {
        }
        return content;
    }

    private void writetoFile(String content, String filepath, String filename) {
        try {
            File floder = new File(filepath);
            // if file doesnt exists, then create it
            if (!floder.exists()) {
                floder.mkdirs();
            }
            File file = new File(filepath + "/" + filename);
            if (!file.exists()) {
                file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        try {
            byte[] buffer = new byte[1024];
            int len = -1;
            while ((len = inStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, len);
                System.out.println(new String(buffer));
            }
        } catch (IOException e) {
        } finally {
            outSteam.close();
            inStream.close();
        }
        return outSteam.toByteArray();
    }
}
