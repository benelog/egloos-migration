package net.benelog.blog.migration

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime

object DateTimes {
    fun fromSeoulToUtc(seoulLocalTime: LocalDateTime): ZonedDateTime {
        val seoulZonedTime = seoulLocalTime.atZone(ZoneId.of("Asia/Seoul"))
        return seoulZonedTime.withZoneSameInstant(ZoneOffset.UTC)
    }
}
