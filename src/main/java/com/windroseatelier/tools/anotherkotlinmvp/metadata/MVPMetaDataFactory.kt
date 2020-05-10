package com.windroseatelier.tools.anotherkotlinmvp.metadata

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

object MVPMetaDataFactory {
    fun getMetaData(name: String, prefix: String, selectedFile: VirtualFile, project: Project): MVPMetaData {
        var prefix = prefix
        val isFragment = name.endsWith("Fragment") || name.endsWith("fragment")
        val path: String = selectedFile.getPath().toString() + "/" + prefix.toLowerCase()
        val packageName: String = path.substring(path.indexOf("java") + 5, path.length()).replace("/", ".")
        prefix = prefix.substring(0, 1).toUpperCase() + prefix.substring(1)
        val rootPackage: String = path.substring(path.indexOf("java") + 5, path.indexOf("/view")).replace("/", ".")
        val layoutpath: String = project.getBasePath().toString() + "/app/src/main/res/layout/"
        System.out.print("Generating MVP for$prefix----$packageName")
        return MVPMetaData(name, prefix, packageName, path, rootPackage, layoutpath, isFragment)
    }
}