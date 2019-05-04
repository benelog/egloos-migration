package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.DisqusThread
import org.springframework.batch.item.ItemStreamWriter
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import javax.xml.stream.XMLEventFactory

object DisqusThreadWriterBuilder {
    fun build(outputPath: Resource): ItemStreamWriter<DisqusThread> {
        val eventFactory = XMLEventFactory.newFactory();

        return StaxEventItemWriterBuilder<DisqusThread>()
                .name("disqusThreadWriter")
                .marshaller(Jaxb2Marshaller().apply {
                    setClassesToBeBound(DisqusThread::class.java)
                })
                .rootElementAttributes(
                        mapOf(
                                "version" to "2.0",
                                "xmlns:content" to "http://purl.org/rss/1.0/modules/content/",
                                "xmlns:dsq" to "http://www.disqus.com/",
                                "xmlns:dc" to "http://purl.org/dc/elements/1.1/",
                                "xmlns:wp" to "http://wordpress.org/export/1.0/"
                        )
                )
                .rootTagName("rss")
                .headerCallback {
                    it.add(eventFactory.createStartElement("", "", "channel"))
                }
                .footerCallback {
                    it.add(eventFactory.createEndElement("", "", "channel"))
                }
                .resource(outputPath)
                .build()
                .apply {
                    afterPropertiesSet()
                }
    }
}
