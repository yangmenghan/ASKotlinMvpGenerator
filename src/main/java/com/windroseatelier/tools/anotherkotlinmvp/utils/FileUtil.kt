package com.windroseatelier.tools.anotherkotlinmvp.utils

import java.io.*

object FileUtil {
    fun readFile(filename: String): String {
        val input = FileUtil::class.java.getResourceAsStream("code/$filename")
        return runCatching { String(readStream(input)) }.getOrDefault("")
    }

    fun writeToFile(content: String, filePath: String, fileName: String) {
        try {
            val floder = File(filePath)
            // if file doesn't exists, then create it
            if (!floder.exists()) floder.mkdirs()
            val file = File("$filePath/$fileName")

            if (!file.exists()) file.createNewFile()
            val fw = FileWriter(file.absoluteFile)
            val bw = BufferedWriter(fw)
            bw.write(content)
            bw.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    fun readStream(inStream: InputStream): ByteArray {
        val outSteam = ByteArrayOutputStream()
        try {
            val buffer = ByteArray(1024)
            var len = -1
            while (inStream.read(buffer).also { len = it } != -1) {
                outSteam.write(buffer, 0, len)
                println(message = String(buffer))
            }
        } catch (e: IOException) {
        } finally {
            outSteam.close()
            inStream.close()
        }
        return outSteam.toByteArray()
    }
}