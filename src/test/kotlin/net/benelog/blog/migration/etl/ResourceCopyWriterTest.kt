package net.benelog.blog.migration.etl

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import org.springframework.core.io.UrlResource
import java.nio.file.Path
import java.nio.file.Paths

class ResourceCopyWriterTest {
    @Test
    fun `복사`(@TempDir tempDir: Path) {
        val downloadDir = tempDir.toFile().toString();
        val source = UrlResource("http://api.egloos.com/benelog/post/2999108.xml")
        val writer = ResourceCopyWriter(downloadDir)

        writer.write(mutableListOf(source))

        val destFile = Paths.get(downloadDir, "2999108.xml").toFile()
        assertThat(destFile.exists()).isTrue()
    }
}
