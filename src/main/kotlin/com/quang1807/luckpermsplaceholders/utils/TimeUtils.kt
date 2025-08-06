package com.quang1807.luckpermsplaceholders.utils

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

object TimeUtils {

    fun formatTimeUntil(expiry: Instant, dateFormat: String): String {
        val now = Instant.now()
        val duration = Duration.between(now, expiry)

        if (duration.isNegative) {
            return "Expired"
        }

        val totalSeconds = duration.seconds

        return when {
            totalSeconds < 60 -> "${totalSeconds}s"
            totalSeconds < 3600 -> {
                val minutes = totalSeconds / 60
                val seconds = totalSeconds % 60
                "${minutes}m ${seconds}s"
            }
            totalSeconds < 86400 -> {
                val hours = totalSeconds / 3600
                val minutes = (totalSeconds % 3600) / 60
                val seconds = totalSeconds % 60
                "${hours}h ${minutes}m ${seconds}s"
            }
            else -> {
                val days = totalSeconds / 86400
                val hours = (totalSeconds % 86400) / 3600
                val minutes = (totalSeconds % 3600) / 60
                val seconds = totalSeconds % 60
                "${days}d ${hours}h ${minutes}m ${seconds}s"
            }
        }
    }

    fun formatExpiryDate(expiry: Instant, dateFormat: String): String {
        val localDateTime = LocalDateTime.ofInstant(expiry, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern(dateFormat)
        return localDateTime.format(formatter)
    }

    fun formatDuration(duration: Duration): String {
        val totalSeconds = duration.seconds

        return when {
            totalSeconds < 60 -> "${totalSeconds} second${if (totalSeconds != 1L) "s" else ""}"
            totalSeconds < 3600 -> {
                val minutes = totalSeconds / 60
                "${minutes} minute${if (minutes != 1L) "s" else ""}"
            }
            totalSeconds < 86400 -> {
                val hours = totalSeconds / 3600
                "${hours} hour${if (hours != 1L) "s" else ""}"
            }
            else -> {
                val days = totalSeconds / 86400
                "${days} day${if (days != 1L) "s" else ""}"
            }
        }
    }

    fun isExpired(expiry: Instant): Boolean {
        return expiry.isBefore(Instant.now())
    }

    fun getRemainingTime(expiry: Instant): Duration {
        return Duration.between(Instant.now(), expiry)
    }
}