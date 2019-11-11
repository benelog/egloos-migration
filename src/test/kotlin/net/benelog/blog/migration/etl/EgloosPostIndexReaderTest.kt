package net.benelog.blog.migration.etl

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.springframework.core.io.ClassPathResource

class EgloosPostIndexReaderTest {
    @Test
    fun `XML 파싱이 잘 된다`() {
        val resource = ClassPathResource("post.xml")
        val reader = PostIndexReader("benelog").buildItemReader(resource)
        var count = 0
        while (true) {
            val item = reader.read()
            if (item == null) {
                break
            }
            assertThat(item.postNo).isNotEqualTo(0)
            println(item.postNo)
            count++
        }
        assertThat(count).isEqualTo(10)
    }

    @Test
    fun `API 호출이 잘 된다`() {
        val reader = PostIndexReader("benelog")
        reader.initReader()

        while (true) {
            val item = reader.read()
            if (item == null) {
                break
            }
            assertThat(item.postNo).isNotEqualTo(0)
            println(item.postNo)
        }
    }
}
