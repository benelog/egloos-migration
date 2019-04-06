package net.benelog.blog.migration

import net.benelog.blog.migration.etl.*
import net.benelog.blog.migration.item.Post
import net.benelog.blog.migration.item.PostIndex
import org.slf4j.LoggerFactory
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.batch.item.ItemStreamReader
import org.springframework.batch.item.ItemWriter
import org.springframework.batch.item.file.builder.MultiResourceItemReaderBuilder
import org.springframework.batch.item.xml.StaxEventItemReader
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver
import org.springframework.oxm.jaxb.Jaxb2Marshaller

@Configuration
class ConvertToAsciidocJobConfig(
        private val stepFactory: StepBuilderFactory,
        private val jobFactory: JobBuilderFactory,

        @Value("\${downloadLocation}")
        private val downloadLocation: String,

        @Value("\${outputLocation}")
        private val outputLocation: String
) {
    @Bean
    fun convertToAsciiDocJob(): Job {
        return jobFactory.get("convertToAsciiDoc")
                .incrementer(RunIdIncrementer())
                .start(convertToAsciiDocStep())
                .build()
    }

    @Bean
    @JobScope
    fun convertToAsciiDocStep(): TaskletStep {
        val resourceResolver = PathMatchingResourcePatternResolver()
        val xmlFiles = resourceResolver.getResources("file:${downloadLocation}*.xml")

        return stepFactory.get("convertToAsciiDocStep")
                .chunk<Post, Resource>(1)
                .reader(MultiFilePostReaderBuilder().multiFilePostReader(xmlFiles))
                .processor(JbakeAsciiDocProcessor())
                .writer(ResourceCopyWriter(outputLocation).apply { initDirectory() })
                .build()
    }

}
