package com.sports.crichunt.utils

import android.widget.TextView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun getDate(textView: TextView, date: String) {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val formattedDate = dateFormat.parse(date)
        textView.text =
            SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(formattedDate)
    } catch (e: ParseException) {
    }
}

fun Date.dateToString(format: String): String {
    val dateFormatter = SimpleDateFormat(format, Locale.getDefault())
    return dateFormatter.format(this)
}

fun stringDateToMillis(date: String): Long? {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    dateFormat.timeZone = TimeZone.getTimeZone("GMT")
    try {
        val formattedDate = dateFormat.parse(date)
        if (formattedDate != null) {
            return formattedDate.time
        }
        return null
    } catch (e: ParseException) {
        return null
    }
}

fun getCountDownString(remainingMillis: Long): String {
    var millis = remainingMillis
    val days = TimeUnit.MILLISECONDS.toDays(millis)
    millis -= TimeUnit.DAYS.toMillis(days)

    val hours: Long = TimeUnit.MILLISECONDS.toHours(millis)
    millis -= TimeUnit.HOURS.toMillis(hours)

    val minutes: Long =
        TimeUnit.MILLISECONDS.toMinutes(millis)
    millis -= TimeUnit.MINUTES.toMillis(minutes)

    val seconds: Long =
        TimeUnit.MILLISECONDS.toSeconds(millis)
    var countDown = ""
    if (days != 0L) {
        countDown += "${days}d "
    }
    if (hours != 0L) {
        countDown += "${hours}h "
    }
    countDown += "${minutes}m "
    countDown += "${seconds}s "
    return countDown
}