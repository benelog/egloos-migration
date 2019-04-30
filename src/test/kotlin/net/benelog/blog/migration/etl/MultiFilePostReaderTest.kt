package net.benelog.blog.migration.etl

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.batch.item.ExecutionContext
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.Resource
import java.time.format.DateTimeFormatter

class MultiFilePostReaderTest {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Test
    fun `여러 파일이 파싱이 된다`() {

        val xmlFiles : Array<Resource> = arrayOf(
                ClassPathResource("detail/2999108.xml"),
                ClassPathResource("detail/3134907.xml")
        )

        val reader = MultiFilePostReaderBuilder.build(xmlFiles)

        reader.open(ExecutionContext())

        with(reader.read()) {
            this!!
            assertThat(title).isEqualTo("Java에서 여러줄에 걸친 문자열 선언을 편하게 하는 @Multiline")
            assertThat(nick).isEqualTo("정상혁")
            assertThat(tags).isEqualTo("java,multiline-string,multiline")
            assertThat(createdAt.format(formatter)).isEqualTo("2013-01-29 07:29:04")
        }

        with(reader.read()) {
            this!!
            assertThat(title).isEqualTo("'네이버를 만든 기술, 읽으면서 배운다 - 자바편' 출간")
            assertThat(nick).isEqualTo("정상혁")
            assertThat(tags).isEqualTo("Java,Naver")
            assertThat(createdAt.format(formatter)).isEqualTo("2015-02-25 13:26:16")
        }
    }
}
