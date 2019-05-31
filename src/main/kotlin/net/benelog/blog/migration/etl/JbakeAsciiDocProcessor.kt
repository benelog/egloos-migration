package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.EgloosPost
import org.springframework.batch.item.ItemProcessor
import org.springframework.core.io.InputStreamResource
import org.springframework.core.io.Resource
import java.time.format.DateTimeFormatter

class JbakeAsciiDocProcessor(
        val converter: HtmlToAsciiDocConverter = HtmlToAsciiDocConverter()
) : ItemProcessor<EgloosPost, Resource> {

    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    override fun process(post: EgloosPost): Resource {
        val content = convert(post)

        return object : InputStreamResource(content.byteInputStream()) {
            override fun getFilename() = "${post.no}.adoc"
        }
    }

    private fun convert(post: EgloosPost): String {
        val asciiDoc = converter.convert(post.content)
        return """= ${post.title}
${post.nick}
${post.createdAt.format(formatter)}
:jbake-type: post
:jbake-status: published
:jbake-tags: ${post.tags}
:idprefix:

$asciiDoc
"""
        // String을 inputStream으로 읽어서쓰니 trimIndent가 안 먹음.
    }
}
