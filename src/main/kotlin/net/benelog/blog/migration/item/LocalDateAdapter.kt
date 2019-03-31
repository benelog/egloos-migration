package net.benelog.blog.migration.item

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.adapters.XmlAdapter

class LocalDateAdapter : XmlAdapter<String, LocalDateTime>() {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Throws(Exception::class)
    override fun unmarshal(value: String): LocalDateTime {

        return LocalDateTime.parse(value, formatter)
    }

    @Throws(Exception::class)
    override fun marshal(value: LocalDateTime): String {
        return value.toString()
    }
}