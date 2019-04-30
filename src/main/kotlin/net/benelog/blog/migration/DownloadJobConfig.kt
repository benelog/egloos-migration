package net.benelog.blog.migration

import net.benelog.blog.migration.etl.PostIndexReader
import net.benelog.blog.migration.etl.PostIndexToResourceProcessor
import net.benelog.blog.migration.etl.ResourceCopyWriter
import net.benelog.blog.migration.item.EgloosPostIndex
import org.springframework.batch.core.Job
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.JobScope
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.core.step.tasklet.TaskletStep
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource


@Configuration
class DownloadJobConfig(
        private val stepFactory: StepBuilderFactory,
        private val jobFactory: JobBuilderFactory,
        @Value("\${downloadLocation}")
        private val downloadLocation: String
) {

    @Bean
    fun downloadJob(): Job {
        return jobFactory.get("download")
                .incrementer(RunIdIncrementer())
                .start(downloadStep(""))
                .build()
    }

    @Bean
    @JobScope
    fun downloadStep(
            @Value("#{jobParameters[egloosAccount]}")
            egloosAccount : String): TaskletStep {
        return stepFactory.get("downloadStep")
                .chunk<EgloosPostIndex, Resource>(1)
                .reader(PostIndexReader(egloosAccount).apply{initReader()})
                .processor(PostIndexToResourceProcessor(egloosAccount))
                .writer(ResourceCopyWriter(downloadLocation).apply{initDirectory()})
                .build()
    }
}
