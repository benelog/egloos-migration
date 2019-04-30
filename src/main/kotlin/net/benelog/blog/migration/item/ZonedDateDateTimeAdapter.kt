package net.benelog.blog.migration.item

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import javax.xml.bind.annotation.adapters.XmlAdapter

class ZonedDateDateTimeAdapter : XmlAdapter<String, ZonedDateTime>() {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    @Throws(Exception::class)
    override fun unmarshal(value: String): ZonedDateTime {
        return ZonedDateTime.parse(value, formatter)
    }

    @Throws(Exception::class)
    override fun marshal(value: ZonedDateTime): String {
        return formatter.format(value)
    }
}
