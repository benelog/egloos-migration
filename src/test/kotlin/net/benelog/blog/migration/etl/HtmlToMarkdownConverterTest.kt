package net.benelog.blog.migration.etl

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test


class HtmlToMarkdownConverterTest {

    @Test
    fun `HTML이 Markdown으로 변환된다`() {
        val html = "<h2>Hello</h2>"

        val markdown = HtmlToMarkdownConverter().convert(html)

        println(html)
        assertThat(markdown).isEqualToIgnoringNewLines("## Hello")
    }
}
