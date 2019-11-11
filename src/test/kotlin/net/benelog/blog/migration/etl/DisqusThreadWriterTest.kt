package net.benelog.blog.migration.etl

import java.nio.file.Path
import java.nio.file.Paths
import java.time.ZoneOffset
import java.time.ZonedDateTime
import net.benelog.blog.migration.item.DisqusThread
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.batch.item.ExecutionContext
import org.springframework.core.io.FileSystemResource

class DisqusThreadWriterTest {

    @Test
    fun test(@TempDir tempDir: Path) {
        val xmlPath = Paths.get(tempDir.toString(), "comments.xml")
        val xmlResource = FileSystemResource(xmlPath)
        val writer = DisqusThreadWriterBuilder.build(xmlResource)

        val thread = DisqusThread().apply {
            title = "Jbake를 선택한 이유"
            link = "https://blog.benelog.net/jbake.html"
            comments = listOf(
                DisqusThread.DisqusComment().apply {
                    author = "정아빠"
                    authorUrl = "https://blog.benelog.net"
                },
                DisqusThread.DisqusComment().apply {
                    author = "정아빠2"
                    authorUrl = "https://blog.benelog.net"
                }
            )
            postDate = ZonedDateTime.now(ZoneOffset.UTC)
        }

        writer.open(ExecutionContext())
        writer.write(listOf(thread))
        writer.close()

        println(xmlResource.file.readText())
    }
}
