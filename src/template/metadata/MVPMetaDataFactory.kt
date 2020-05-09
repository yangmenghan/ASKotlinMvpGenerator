package template.metadata;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;

public class MVPMetaDataFactory {
    public static MVPMetaData getMetaData(String name, String prefix, VirtualFile selectedFile, Project project) {
        boolean isFragment = name.endsWith("Fragment") || name.endsWith("fragment");

        String path = selectedFile.getPath() + "/" + prefix.toLowerCase();
        String packageName = path.substring(path.indexOf("java") + 5, path.length()).replace("/", ".");

        prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1);
        String rootPackage = path.substring(path.indexOf("java") + 5, path.indexOf("/view")).replace("/", ".");
        String layoutpath = project.getBasePath() + "/app/src/main/res/layout/";

        System.out.print("Generating MVP for" + prefix + "----" + packageName);

        return new MVPMetaData(name, prefix, packageName, path, rootPackage, layoutpath, isFragment);
    }
}
