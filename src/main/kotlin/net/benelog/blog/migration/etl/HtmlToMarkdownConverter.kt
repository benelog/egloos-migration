package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.ExternalProcesses

/** pandoc이 미리 설치되어 있어야함
 */
class HtmlToMarkdownConverter {
    private val command = "pandoc --atx-headers -f html -t markdown"

    fun convert(html: String): String {
        return ExternalProcesses.execute(command, html)
    }
}
