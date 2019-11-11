package net.benelog.blog.migration.etl

import java.time.format.DateTimeFormatter
import net.benelog.blog.migration.item.EgloosPost
import org.springframework.batch.item.ItemProcessor
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource

class JbakeAsciiDocProcessor(
    private val converter: HtmlToAsciiDocConverter = HtmlToAsciiDocConverter()
) : ItemProcessor<EgloosPost, Resource> {

    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun process(post: EgloosPost): Resource {
        val content = convert(post)

        return object : InputStreamResource(content.byteInputStream()) {
            override fun getFilename() = "${post.no}.adoc"
        }
    }

    private fun convert(post: EgloosPost): String {
        val asciiDoc = converter.convert(post.content)
        return """
            = ${post.title}
            ${post.nick}
            ${post.createdAt.format(formatter)}
            :jbake-type: post
            :jbake-status: published
            :jbake-tags: ${post.tags}
            :idprefix:
        """.trimIndent() + asciiDoc
        // asciiDoc 컨텐츠에는 indent가 없기 때문에 마지막에 더해줌.
    }
}
