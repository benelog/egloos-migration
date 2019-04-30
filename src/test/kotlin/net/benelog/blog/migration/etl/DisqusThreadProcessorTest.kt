package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.EgloosPost
import org.junit.jupiter.api.Test
import org.springframework.batch.item.ExecutionContext
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.FileSystemResource
import java.time.LocalDateTime

class DisqusThreadProcessorTest {

    @Test
    fun processAndWrite() {
        val processor = DisqusThreadProcessor(
                {
                    ClassPathResource("/comment_$it.xml")
                },
                "https://blog2.benelog.net"
        )

        val post = EgloosPost().apply {
            title = "개발 수행"
            no = 2999108
            createdAt = LocalDateTime.now()
        }

        val disqusThread = processor.process(post)

        DisqusThreadWriterBuilder.build(FileSystemResource("./out/disqus_comments.xml")).apply {
            open(ExecutionContext())
            write(listOf(disqusThread))
            close()
        }
    }
}
