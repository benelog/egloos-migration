package net.benelog.blog.migration.etl

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test

class HtmlToAsciidocConverterTest {

    @Test
    fun `HTML이 AsciiDoc으로 변환된다`() {
        val html = "<h2>Hello</h2>"

        val adoc = HtmlToAsciiDocConverter().convert(html)

        assertThat(adoc).isEqualToIgnoringNewLines("== Hello")
    }
}
