package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.DisqusThread
import org.springframework.batch.item.ItemStreamWriter
import org.springframework.batch.item.xml.StaxEventItemWriter
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller
import javax.xml.stream.XMLEventFactory

object DisqusThreadWriterBuilder {
    fun build(outputPath: Resource) : ItemStreamWriter<DisqusThread> {
        return StaxEventItemWriter<DisqusThread>().apply {
            setName("build")
            setMarshaller(Jaxb2Marshaller().apply {
                setClassesToBeBound(DisqusThread::class.java)
                val eventFactory = XMLEventFactory.newFactory();
                setHeaderCallback {
                    it.add(eventFactory.createStartElement("", "", "channel"))
                }
                setFooterCallback {
                    it.add(eventFactory.createEndElement("", "", "channel"))
                }
            })

            rootTagName = "rss"
            rootElementAttributes = mapOf(
                    "version" to "2.0",
                    "xmlns:content" to "http://purl.org/rss/1.0/modules/content/",
                    "xmlns:dsq" to "http://www.disqus.com/",
                    "xmlns:dc" to "http://purl.org/dc/elements/1.1/",
                    "xmlns:wp" to "http://wordpress.org/export/1.0/"
            )
            setResource(outputPath)
            afterPropertiesSet()
        }
    }
}