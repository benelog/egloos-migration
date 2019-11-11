package net.benelog.blog.migration.item

import java.time.LocalDateTime
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlRootElement(name = "post")
@XmlAccessorType(XmlAccessType.FIELD)
class EgloosPost() {
    @field:XmlElement(name = "post_no")
    var no: Int = 0

    @field:XmlElement(name = "post_title")
    lateinit var title: String

    @field:XmlElement(name = "post_content")
    lateinit var content: String

    @field:XmlElement(name = "category_name")
    lateinit var categoryName: String

    @field:XmlElement(name = "post_nick")
    lateinit var nick: String

    @field:XmlElement(name = "post_tags")
    lateinit var tags: String

    @field:XmlElement(name = "post_date_created")
    @field:XmlJavaTypeAdapter(value = LocalDateAdapter::class)
    lateinit var createdAt: LocalDateTime

    @field:XmlElement(name = "post_date_modified")
    @field:XmlJavaTypeAdapter(value = LocalDateAdapter::class)
    lateinit var modifiedAt: LocalDateTime
}
