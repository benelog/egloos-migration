package net.benelog.blog.migration

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import javax.sql.DataSource

@SpringBootApplication
@EnableBatchProcessing
class MigrationApplication

fun main(args: Array<String>) {
    val context = runApplication<MigrationApplication>(*args)
    val exitCode = SpringApplication.exit(context)
    System.exit(exitCode);
}

@Bean
fun jobMetaDb(): DataSource {
    return DataSourceBuilder.create()
            .driverClassName("org.h2.Driver")
            .url("jdbc:h2:~/elgoos-migraion/job;AUTO_SERVER=TRUE")
            .build()
}
