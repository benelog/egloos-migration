package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.Comment
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import java.time.format.DateTimeFormatter

class CommentParseTest {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Test
    fun `comment 파싱이 잘된다`() {
        val xmlFile = ClassPathResource("comment_2999108.xml")
        val reader = buildReader(xmlFile)

        reader.open(ExecutionContext())
        with(reader.read()) {
            assertThat(no).isEqualTo("5216791")
            assertThat(nick).isEqualTo("만숙이바보")
            assertThat(createdAt.format(formatter)).isEqualTo("2013-01-31 19:25:23")
            assertThat(writerThumbnail).isEqualTo("http://md.egloos.com/img/eg/profile_anonymous.jpg")
        }
    }

    private fun buildReader(xmlFile: Resource): StaxEventItemReader<Comment> {
        return StaxEventItemReaderBuilder<Comment>()
                .name("commentReader")
                .resource(xmlFile)
                .unmarshaller(Jaxb2Marshaller().apply {
                    setClassesToBeBound(Comment::class.java)
                })
                .addFragmentRootElements(listOf("item"))
                .build().apply {
                    afterPropertiesSet()
                }
    }
}
