package com.windroseatelier.tools.anotherkotlinmvp.utils

import java.util.regex.Pattern

object CaseUtil {
    fun camelToSnakeCase(line: String?): String {
        if (line.isNullOrEmpty()) return ""

        val name = line[0].toUpperCase() + line.substring(1)
        val sb = StringBuffer()
        val pattern = Pattern.compile("[A-Z]([a-z\\d]+)?")
        val matcher = pattern.matcher(name)
        while (matcher.find()) {
            val word: String = matcher.group()
            sb.append(word.toLowerCase())
            sb.append(if (matcher.end() == name.length) "" else "_")
        }
        return sb.toString()
    }
}