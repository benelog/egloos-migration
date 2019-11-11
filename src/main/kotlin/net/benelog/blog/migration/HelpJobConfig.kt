package net.benelog.blog.migration

import org.springframework.batch.core.Job
import org.springframework.batch.core.Step
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory
import org.springframework.batch.core.launch.support.RunIdIncrementer
import org.springframework.batch.repeat.RepeatStatus
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HelpJobConfig(
    private val stepFactory: StepBuilderFactory,
    private val jobFactory: JobBuilderFactory,
    @Value("\${downloadLocation}")
    private val downloadLocation: String
) {

    @Bean
    fun helpJob(): Job {
        return jobFactory.get("help")
            .incrementer(RunIdIncrementer())
            .start(printStep())
            .build()
    }

    @Bean
    fun printStep(): Step {
        return stepFactory.get("printStep")
            .tasklet { _, _ ->
                println(message)
                println("다운로드 위치 : $downloadLocation")
                RepeatStatus.FINISHED
            }
            .build()
    }

    private val message = """
        ## 안내
        실행할 작업은 `-Dspring.batch.job.names=help`와 같이 VM option으로 지정한다"
        자세한 사용법은 https://github.com/benelog/egloos-migration 을 참조한다
    """.trimIndent()
}
