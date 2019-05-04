package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.ExternalProcesses

/** pandoc이 미리 설치되어 있어야함
 * 한줄짜리 헤더 스테일을 쓰려면 pandoc 최신버전을 권장함
 * ( "예) == title -> <h2>title</h2>" )
 * https://github.com/jgm/pandoc/issues/5038
 */
class HtmlToAsciiDocConverter {
    private val command = "pandoc --wrap=none -f html -t asciidoc"

    fun convert(html: String): String {
        return ExternalProcesses.execute(command, html)
    }
}
