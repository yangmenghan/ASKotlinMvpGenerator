package com.windroseatelier.tools.anotherkotlinmvp.metadata

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object MVPMetaDataFactory {
    fun getMetaData(name: String, prefix: String, selectedFile: VirtualFile, project: Project): MVPMetaData {
        val isFragment = name.endsWith("Fragment") || name.endsWith("fragment")
        val path = selectedFile.path + "/" + prefix.toLowerCase()
        val packageName = path.substring(path.indexOf("java") + 5, path.length).replace("/", ".")
        val formattedPrefix = prefix[0].toUpperCase() + prefix.substring(1)
        val rootPackage = path.substring(path.indexOf("java") + 5, path.indexOf("/view")).replace("/", ".")
        val layoutPath = project.basePath.toString() + "/app/src/main/res/layout/"

        print("Generating MVP for$formattedPrefix----$packageName")
        return MVPMetaData(name, formattedPrefix, packageName, path, rootPackage, layoutPath, isFragment)
    }
}