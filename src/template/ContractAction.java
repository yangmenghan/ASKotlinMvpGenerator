package template;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.actionSystem.PlatformDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import template.metadata.MVPMetaData;
import template.metadata.MVPMetaDataFactory;

import java.io.InputStream;
import java.util.List;

public class ContractAction extends AnAction {
    Project project;
    VirtualFile selectedFile;
    PsiFile selectedPsiFile;
    MVPMetaData metaData;

    List<String> viewMethods;
    List<String> presenterMethods;

    @Override
    public void actionPerformed(AnActionEvent e) {
        project = e.getProject();
        selectedFile = e.getData(PlatformDataKeys.VIRTUAL_FILE);
        selectedPsiFile = e.getData(LangDataKeys.PSI_FILE);

        if (selectedFile == null || selectedPsiFile == null) {
            Messages.showErrorDialog("No file selected", "Error");
        } else if (!isContractFile()) {
            Messages.showErrorDialog("Not a MVP contract", "Error");
        } else {
            String fileName = selectedFile.getName();
            String prefix = fileName.substring(0, fileName.indexOf("Contract"));
            metaData = MVPMetaDataFactory.getMetaData(fileName, prefix, selectedFile, project);
            createMvp();
            project.getBaseDir().refresh(false, true);
        }
    }

    private boolean isContractFile() {
        return selectedFile.exists() &&
                selectedFile.getName().endsWith("Contract") &&
                selectedFile.getExtension().equals("kt");
    }

    private void createMvp() {
//        InputStream file = selectedFile.getInputStream();
//        selectedPsiFile.get
    }
}

