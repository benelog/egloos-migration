package net.benelog.blog.migration.etl

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.UrlResource
import java.nio.file.Path
import java.nio.file.Paths

class ResourceCopyWriterTest {
    @Test
    fun `URL로부터 복사`(@TempDir tempDir: Path) {
        val downloadDir = tempDir.toFile().toString();
        val source = UrlResource("http://api.egloos.com/benelog/post/2999108.xml")
        val writer = ResourceCopyWriter(downloadDir)

        writer.write(mutableListOf(source))

        val destFile = Paths.get(downloadDir, "2999108.xml").toFile()
        assertThat(destFile.exists()).isTrue()
    }

    @Test
    fun `String으로부터 복사`(@TempDir tempDir: Path) {
        val downloadDir = tempDir.toFile().toString();
        val fileName = "test.txt"
        val content = "hello!"
        val source = object : ByteArrayResource(content.toByteArray(Charsets.UTF_8)) {
            override fun getFilename() = fileName
        }
        val writer = ResourceCopyWriter(downloadDir)

        writer.write(mutableListOf(source))

        val destFile = Paths.get(downloadDir, fileName).toFile()
        assertThat(destFile.exists()).isTrue()
        val writtenContent = destFile.readText(Charsets.UTF_8)
        assertThat(writtenContent).isEqualTo(content)
    }
}
