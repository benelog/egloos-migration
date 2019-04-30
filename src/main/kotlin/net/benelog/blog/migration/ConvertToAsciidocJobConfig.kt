package net.benelog.blog.migration

import net.benelog.blog.migration.etl.*
import net.benelog.blog.migration.item.EgloosPost
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.core.io.support.PathMatchingResourcePatternResolver

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
    fun convertToAsciiDocStep(): TaskletStep {
        val resourceResolver = PathMatchingResourcePatternResolver()
        val xmlFiles = resourceResolver.getResources("file:${downloadLocation}*.xml")

        return stepFactory.get("convertToAsciiDocStep")
                .chunk<EgloosPost, Resource>(1)
                .reader(MultiFilePostReaderBuilder.build(xmlFiles))
                .processor(JbakeAsciiDocProcessor())
                .writer(ResourceCopyWriter(outputLocation).apply { initDirectory() })
                .build()
    }

}
