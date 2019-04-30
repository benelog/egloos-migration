package net.benelog.blog.migration.etl

import org.slf4j.LoggerFactory
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource

class EgloosCommentResourceProvider(val egloosDomain: String) : (Int) -> Resource {
    private val log = LoggerFactory.getLogger(this::class.java)

    override fun invoke(postNo: Int): Resource {
        val apiUrl = "http://api.egloos.com/$egloosDomain/post/$postNo/comment.xml"
        log.info("API call to : {}" , apiUrl)
        return UrlResource(apiUrl)
    }
}
