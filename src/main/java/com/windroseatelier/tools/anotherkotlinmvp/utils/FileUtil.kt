package com.windroseatelier.tools.anotherkotlinmvp.utils

import java.io.*

object FileUtil {
    fun readFile(filename: String): String {
        val input: InputStream = FileUtil::class.java.getResourceAsStream("code/$filename")
        var content = ""
        try {
            content = String(readStream(input))
        } catch (e: Exception) {
        }
        return content
    }

    fun writeToFile(content: String?, filepath: String, filename: String) {
        try {
            val floder = File(filepath)
            // if file doesnt exists, then create it
            if (!floder.exists()) {
                floder.mkdirs()
            }
            val file = File("$filepath/$filename")
            if (!file.exists()) {
                file.createNewFile()
            }
            val fw = FileWriter(file.getAbsoluteFile())
            val bw = BufferedWriter(fw)
            bw.write(content)
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @kotlin.jvm.Throws(Exception::class)
    fun readStream(inStream: InputStream): ByteArray {
        val outSteam = ByteArrayOutputStream()
        try {
            val buffer = ByteArray(1024)
            var len = -1
            while (inStream.read(buffer).also({ len = it }) != -1) {
                outSteam.write(buffer, 0, len)
                System.out.println(String(buffer))
            }
        } catch (e: IOException) {
        } finally {
            outSteam.close()
            inStream.close()
        }
        return outSteam.toByteArray()
    }
}