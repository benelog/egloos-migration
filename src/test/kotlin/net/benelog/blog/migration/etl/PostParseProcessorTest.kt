package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.Post
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import java.time.format.DateTimeFormatter

class PostParseProcessorTest {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Test
    fun `파싱이 된다`() {
        val resource = ClassPathResource("detail/2999108.xml")
        val reader = buildItemReader(resource)

        reader.open(ExecutionContext())
        val post = reader.read()

        with(post) {
            assertThat(title).isEqualTo("Java에서 여러줄에 걸친 문자열 선언을 편하게 하는 @Multiline")
            assertThat(nick).isEqualTo("정상혁")
            assertThat(tags).isEqualTo("java,multiline-string,multiline")
            assertThat(createdAt.format(formatter)).isEqualTo("2013-01-29 07:29:04")
           }
    }

    private fun buildItemReader(resource: Resource): StaxEventItemReader<Post> {
        return StaxEventItemReaderBuilder<Post>()
                .resource(resource)
                .name(resource.toString())
                .unmarshaller(Jaxb2Marshaller().apply {
                    setClassesToBeBound(Post::class.java)
                })
                .addFragmentRootElements("post")
                .build().apply {
                    afterPropertiesSet()
                }
    }
}