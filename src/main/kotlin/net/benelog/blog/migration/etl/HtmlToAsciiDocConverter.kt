package net.benelog.blog.migration.etl

/** pandoc이 미리 설치되어 있어야함
 * 한줄짜리 헤더 스테일을 쓰려면 pandoc 최신버전을 권장함
 * ( "예) == title -> <h2>title</h2>" )
 * https://github.com/jgm/pandoc/issues/5038
 */
class HtmlToAsciiDocConverter {
    private val command = "pandoc --wrap=none -f html -t asciidoc".split(" ")

    fun convert(html: String): String {
        val process = ProcessBuilder(command).run {
            redirectErrorStream(true)
            return@run start();
        }

        process.outputStream.run {
            write(html.toByteArray())
            close()
        }

        process.waitFor()
        return process.inputStream.bufferedReader().use {
            it.readText();
        }
    }
}
