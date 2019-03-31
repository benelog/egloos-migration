package net.benelog.blog.migration.item

import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "item")
data class PostIndex (
        @set:XmlElement(name = "post_no")
        var postNo: Int = 0
)
