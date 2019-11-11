package net.benelog.blog.migration.etl

import net.benelog.blog.migration.item.EgloosPostIndex
import org.springframework.batch.item.ItemProcessor
import org.springframework.core.io.Resource
import org.springframework.core.io.UrlResource

class PostIndexToResourceProcessor(val egloosAccount: String) : ItemProcessor<EgloosPostIndex, Resource> {
    override fun process(item: EgloosPostIndex): Resource? {
        return UrlResource("http://api.egloos.com/$egloosAccount/post/${item.postNo}.xml")
    }
}
