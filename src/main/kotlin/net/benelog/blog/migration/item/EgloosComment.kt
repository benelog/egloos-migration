package net.benelog.blog.migration.item

import java.time.LocalDateTime
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
class EgloosComment() {
    @field:XmlElement(name = "comment_no")
    lateinit var no: String

    @field:XmlElement(name = "comment_content")
    lateinit var content: String

    @field:XmlElement(name = "comment_writer")
    lateinit var writer: String

    @field:XmlElement(name = "comment_nick")
    lateinit var nick: String

    @field:XmlElement(name = "comment_writer_url")
    lateinit var writerUrl: String

    @field:XmlElement(name = "comment_writer_homepage")
    lateinit var writerHomepage: String

    @field:XmlElement(name = "comment_writer_thumbnail")
    lateinit var writerThumbnail: String

    @field:XmlElement(name = "comment_depth")
    var depth: Int = 0

    @field:XmlElement(name = "comment_hidden")
    var hidden: Int = 0

    @field:XmlElement(name = "comment_date_created")
    @field:XmlJavaTypeAdapter(value = LocalDateAdapter::class)
    lateinit var createdAt: LocalDateTime
}
