package net.benelog.blog.migration.etl

import java.io.File
import javax.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemWriter
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.Resource
import org.springframework.core.io.WritableResource

class ResourceCopyWriter(val destDir: String) : ItemWriter<Resource> {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun write(items: List<Resource>) {
        for (source in items) {
            val destFile = FileSystemResource("$destDir/$source.filename")
            copy(source, destFile)
        }
    }

    private fun copy(source: Resource, destination: WritableResource) {
        source.inputStream.use { input ->
            destination.outputStream.use { output ->
                input.copyTo(output)
            }
        }
        log.info("copy {} to {}", source, destination)
    }

    @PostConstruct
    public fun initDirectory() {
        val dir = File(destDir)
        if (dir.exists() && dir.isDirectory) {
            return
        }
        dir.mkdirs()
    }
}
