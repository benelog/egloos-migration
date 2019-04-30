package net.benelog.blog.migration

import java.time.*

object DateTimes {
    fun fromSeoulToUtc(seoulLocalTime: LocalDateTime) : ZonedDateTime {
        val seoulZonedTime = seoulLocalTime.atZone(ZoneId.of("Asia/Seoul"))
        return seoulZonedTime.withZoneSameInstant(ZoneOffset.UTC);
    }
}