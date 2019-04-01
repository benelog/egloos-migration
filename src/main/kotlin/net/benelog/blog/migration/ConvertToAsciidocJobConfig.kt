package net.benelog.blog.migration

import net.benelog.blog.migration.item.Post
import org.springframework.batch.item.ItemStreamReader
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller

class ConvertToAsciidocJobConfig {

    @Bean
    fun postMultiFileReader(xmlFiles: Array<Resource>): ItemStreamReader<Post> {
        return MultiResourceItemReaderBuilder<Post>()
                .name("postMultiFileReader")
                .resources(xmlFiles)
                .delegate(buildDelegate())
                .build()
    }

    private fun buildDelegate(): StaxEventItemReader<Post> {
        return StaxEventItemReader<Post>().apply {
            setName("postFileReader")
            setUnmarshaller(Jaxb2Marshaller().apply {
                setClassesToBeBound(Post::class.java)
            })
            setFragmentRootElementName("post")
        }
    }
}