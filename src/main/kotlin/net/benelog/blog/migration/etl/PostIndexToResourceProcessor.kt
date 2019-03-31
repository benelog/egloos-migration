package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.PostIndex
import org.springframework.batch.core.ItemProcessListener
import org.springframework.batch.item.ItemProcessor
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource

class PostIndexToResourceProcessor(val blogDomain :String) : ItemProcessor<PostIndex, Resource> {
    override fun process(item: PostIndex): Resource? {
        return UrlResource("http://api.egloos.com/$blogDomain/post/${item.postNo}.xml")
    }
}