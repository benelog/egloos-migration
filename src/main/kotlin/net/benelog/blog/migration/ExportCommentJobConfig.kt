package net.benelog.blog.migration

import net.benelog.blog.migration.etl.DisqusThreadProcessor
import net.benelog.blog.migration.etl.DisqusThreadWriterBuilder
import net.benelog.blog.migration.etl.EgloosCommentResourceProvider
import net.benelog.blog.migration.etl.MultiFilePostReaderBuilder
import net.benelog.blog.migration.item.DisqusThread
import net.benelog.blog.migration.item.EgloosPost
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.FileSystemResource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Configuration
class ExportCommentJobConfig(
        private val stepFactory: StepBuilderFactory,
        private val jobFactory: JobBuilderFactory,
        @Value("\${downloadLocation}")
        private val downloadLocation: String
) {

    private val formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")

    @Bean
    fun exportCommentJob(): Job {
        return jobFactory.get("exportComment")
                .incrementer(RunIdIncrementer())
                .start(exportCommentStep("", ""))
                .build()
    }

    @Bean
    @JobScope
    fun exportCommentStep(@Value("#{jobParameters[egloosAccount]}")
                          egloosAccount: String,
                          @Value("#{jobParameters[targetParentUrl]}")
                          targetParentUrl: String): TaskletStep {
        val resourceResolver = PathMatchingResourcePatternResolver()
        val egloosPostXmls = resourceResolver.getResources("file:${downloadLocation}*.xml")
        val timestamp = formatter.format(LocalDateTime.now())

        val commentProvider = EgloosCommentResourceProvider(egloosAccount)
        val disqusThreadXml = FileSystemResource("./disqusComments_$timestamp.xml")

        return stepFactory.get("exportCommentStep")
                .chunk<EgloosPost, DisqusThread>(10)
                .reader(MultiFilePostReaderBuilder.build(egloosPostXmls))
                .processor(DisqusThreadProcessor(commentProvider, targetParentUrl))
                .writer(DisqusThreadWriterBuilder.build(disqusThreadXml))
                .build()
    }
}
