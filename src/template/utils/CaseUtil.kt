package template.utils

import java.util.regex.Matcher
import java.util.regex.Pattern

object CaseUtil {
    fun camelToSnakeCase(line: String?): String {
        var line = line
        if (line == null || "".equals(line)) {
            return ""
        }
        line = String.valueOf(line.charAt(0)).toUpperCase().concat(line.substring(1))
        val sb = StringBuffer()
        val pattern: Pattern = Pattern.compile("[A-Z]([a-z\\d]+)?")
        val matcher: Matcher = pattern.matcher(line)
        while (matcher.find()) {
            val word: String = matcher.group()
            sb.append(word.toLowerCase())
            sb.append(if (matcher.end() === line.length()) "" else "_")
        }
        return sb.toString()
    }
}