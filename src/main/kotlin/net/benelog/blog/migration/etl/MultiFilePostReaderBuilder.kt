package net.benelog.blog.migration.etl

import net.benelog.blog.migration.ConvertToAsciidocJobConfig
import net.benelog.blog.migration.item.Post
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemStreamReader
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller

class MultiFilePostReaderBuilder {
    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }

    fun multiFilePostReader(xmlFiles: Array<Resource>): ItemStreamReader<Post> {
        log.info("{} 개의 파일을 처리할 예정", xmlFiles.size)

        return MultiResourceItemReaderBuilder<Post>()
                .name("multiFilePostReader")
                .resources(xmlFiles)
                .delegate(buildDelegate())
                .build()
    }

    private fun buildDelegate(): StaxEventItemReader<Post> {
        return StaxEventItemReader<Post>().apply {
            setName("postFileReader")
            setUnmarshaller(Jaxb2Marshaller().apply {
                setClassesToBeBound(Post::class.java)
            })
            setFragmentRootElementName("post")
        }
    }
}