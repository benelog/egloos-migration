package net.benelog.blog.migration.item

import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement

@XmlRootElement(name = "item")
data class EgloosPostIndex(
    @set:XmlElement(name = "post_no")
    var postNo: Int = 0
)
