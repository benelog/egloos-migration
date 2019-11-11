package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.EgloosPostIndex
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemReader
import org.springframework.batch.item.ItemStream
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource
import org.springframework.oxm.jaxb.Jaxb2Marshaller

class PostIndexReader(val egloosAccount: String) : PagingItemReader<EgloosPostIndex>(), ItemStream {
    var page = 0
    var context: ExecutionContext = ExecutionContext()
    val urlReader = mutableListOf<ItemStream>()

    private val log = LoggerFactory.getLogger(this::class.java)

    override fun update(context: ExecutionContext) {}

    override fun open(context: ExecutionContext) {
        this.context = context
    }

    override fun close() {
        urlReader.forEach { it.close() }
        urlReader.clear()

        page = 0
    }

    override fun nextItemReader(): ItemReader<EgloosPostIndex> {
        page++
        val url = "http://api.egloos.com/$egloosAccount/post.xml?page=$page"
        log.info("API url : {}", url)
        return buildItemReader(UrlResource(url))
    }

    fun buildItemReader(resource: Resource): ItemReader<EgloosPostIndex> {
        return StaxEventItemReaderBuilder<EgloosPostIndex>()
            .resource(resource)
            .name(resource.toString())
            .unmarshaller(Jaxb2Marshaller().apply {
                setClassesToBeBound(EgloosPostIndex::class.java)
            })
            .addFragmentRootElements("item")
            .build().apply {
                afterPropertiesSet()
                urlReader.add(this)
                open(context)
            }
    }
}
