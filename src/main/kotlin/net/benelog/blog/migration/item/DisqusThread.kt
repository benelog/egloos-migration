package net.benelog.blog.migration.item

import java.time.ZonedDateTime
import javax.xml.bind.annotation.XmlAccessType
import javax.xml.bind.annotation.XmlAccessorType
import javax.xml.bind.annotation.XmlElement
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter

@XmlRootElement(name = "item")
@XmlAccessorType(XmlAccessType.FIELD)
class DisqusThread {
    lateinit var title: String
    lateinit var link: String

    @field:XmlElement(name = "dsq:thread_identifier")
    lateinit var id: String

    @field:XmlElement(name = "wp:post_date_gmt")
    @field:XmlJavaTypeAdapter(value = ZonedDateDateTimeAdapter::class)
    lateinit var postDate: ZonedDateTime

    @field:XmlElement(name = "wp:post_status")
    val postStatus: String = "open"

    @field:XmlElement(name = "wp:comment")
    lateinit var comments: List<DisqusComment>

    @XmlAccessorType(XmlAccessType.FIELD)
    class DisqusComment {
        @field:XmlElement(name = "wp:comment_id")
        lateinit var id: String

        @field:XmlElement(name = "wp:comment_author")
        lateinit var author: String

        @field:XmlElement(name = "wp:comment_author_url")
        lateinit var authorUrl: String

        @field:XmlElement(name = "wp:comment_author_email")
        var authorEmail: String? = null

        @field:XmlElement(name = "wp:comment_date_gmt")
        @field:XmlJavaTypeAdapter(value = ZonedDateDateTimeAdapter::class)
        lateinit var commentDate: ZonedDateTime

        @field:XmlElement(name = "wp:comment_content")
        lateinit var content: String

        @field:XmlElement(name = "wp:comment_approved")
        val approved: Int = 1 // 1이 승인

        @field:XmlElement(name = "wp:comment_parent")
        var parentId: String? = null

        override fun toString(): String {
            return "DisqusComment(id='$id', author='$author', authorUrl='$authorUrl', authorEmail=$authorEmail, commentDate=$commentDate, content='$content', approved=$approved, parentId=$parentId)"
        }
    }

    override fun toString(): String {
        return "DisqusThread(title='$title', link='$link', id='$id', postDate=$postDate, postStatus='$postStatus', comments=$comments)"
    }
}
