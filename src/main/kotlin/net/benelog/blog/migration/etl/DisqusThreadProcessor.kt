package net.benelog.blog.migration.etl

import net.benelog.blog.migration.DateTimes
import net.benelog.blog.migration.item.DisqusThread
import net.benelog.blog.migration.item.EgloosComment
import net.benelog.blog.migration.item.EgloosPost
import org.slf4j.LoggerFactory
import org.springframework.batch.item.ExecutionContext
import org.springframework.batch.item.ItemProcessor
import org.springframework.batch.item.ItemStreamReader
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder
import org.springframework.core.io.Resource
import org.springframework.oxm.jaxb.Jaxb2Marshaller

class DisqusThreadProcessor(
        val commentProvider: (Int) -> Resource,
        val targetParentUrl: String
) : ItemProcessor<EgloosPost, DisqusThread> {

    private val log = LoggerFactory.getLogger(this::class.java)

    private val emailMapping = mapOf(
            "benelog" to "benelog@gmail.com"
    )

    override fun process(item: EgloosPost): DisqusThread? {

        val postNo = item.no
        val egloosComment = readEgloosComments(postNo)
        if (egloosComment.isEmpty()) {
            return null
        }

        return DisqusThread().apply {
            title = item.title
            link = "$targetParentUrl/$postNo.html"
            id = "$postNo.html"
            postDate = DateTimes.fromSeoulToUtc(item.createdAt)
            comments = toDisqusComment(egloosComment)
        }
    }

    private fun readEgloosComments(postNo: Int): List<EgloosComment> {
        val apiResource = commentProvider(postNo)
        val commentReader = buildReader(apiResource)
        commentReader.open(ExecutionContext())

        val comments = mutableListOf<EgloosComment>();
        while (true) {
            val comment = commentReader.read() ?: break
            comments.add(comment)
        }

        commentReader.close()
        return comments
    }

    private fun buildReader(input: Resource): ItemStreamReader<EgloosComment> {
        return StaxEventItemReaderBuilder<EgloosComment>()
                .name("commentReader")
                .resource(input)
                .unmarshaller(Jaxb2Marshaller().apply {
                    setClassesToBeBound(EgloosComment::class.java)
                })
                .addFragmentRootElements(listOf("item"))
                .build().apply {
                    afterPropertiesSet()
                }
    }

    private fun toDisqusComment(egloosComments: List<EgloosComment>): List<DisqusThread.DisqusComment> {
        return egloosComments.map {
            DisqusThread.DisqusComment().apply {
                id = it.no
                author = if (it.nick.isNotBlank()) it.nick else "Guest"
                commentDate = DateTimes.fromSeoulToUtc(it.createdAt)
                content = it.content
                authorEmail = emailMapping.get(it.writer)
                authorUrl = if (it.writerHomepage.isNotBlank()) it.writerHomepage else it.writerUrl

                if (it.depth == 2) {
                    parentId = it.no.substring(0, it.no.indexOf("."))
                    // commentNo가 5216791.01 이면 5216791이 parent의 번호이다.
                }
            }
        }
    }
}
