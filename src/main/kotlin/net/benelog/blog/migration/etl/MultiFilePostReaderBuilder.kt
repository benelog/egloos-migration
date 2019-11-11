package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.EgloosPost
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ItemStreamReader
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller

object MultiFilePostReaderBuilder {
    private val log = LoggerFactory.getLogger(this::class.java)

    fun build(xmlFiles: Array<Resource>): ItemStreamReader<EgloosPost> {
        log.info("{} 개의 파일을 처리할 예정", xmlFiles.size)

        return MultiResourceItemReaderBuilder<EgloosPost>()
            .name("build")
            .resources(*xmlFiles)
            .delegate(buildDelegate())
            .build()
    }

    private fun buildDelegate(): StaxEventItemReader<EgloosPost> {
        return StaxEventItemReader<EgloosPost>().apply {
            setName("postFileReader")
            setUnmarshaller(Jaxb2Marshaller().apply {
                setClassesToBeBound(EgloosPost::class.java)
            })
            setFragmentRootElementName("post")
        }
    }
}
