package net.benelog.blog.migration.etl

import java.time.format.DateTimeFormatter
import net.benelog.blog.migration.item.EgloosComment
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller

class EgloosCommentParseTest {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Test
    fun `comment 파싱이 잘된다`() {
        val xmlFile = ClassPathResource("comment_2999108.xml")
        val reader = buildReader(xmlFile)

        reader.open(ExecutionContext())
        with(reader.read()) {
            assertThat(this!!.no).isEqualTo("5216791")
            assertThat(nick).isEqualTo("만숙이바보")
            assertThat(createdAt.format(formatter)).isEqualTo("2013-01-31 19:25:23")
            assertThat(writerThumbnail).isEqualTo("http://md.egloos.com/img/eg/profile_anonymous.jpg")
            assertThat(writerHomepage).isEqualTo("")
        }
        reader.close()
    }

    private fun buildReader(xmlFile: Resource): StaxEventItemReader<EgloosComment> {
        return StaxEventItemReaderBuilder<EgloosComment>()
            .name("commentReader")
            .resource(xmlFile)
            .unmarshaller(Jaxb2Marshaller().apply {
                setClassesToBeBound(EgloosComment::class.java)
            })
            .addFragmentRootElements(listOf("item"))
            .build().apply {
                afterPropertiesSet()
            }
    }
}
