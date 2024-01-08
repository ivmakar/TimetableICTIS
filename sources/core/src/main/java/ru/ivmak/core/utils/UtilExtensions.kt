package ru.ivmak.core.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun String.parseDate(): Date? {
    val formatter = SimpleDateFormat("EEE, d MMMM", Locale.forLanguageTag("ru-RU"))
    return formatter.parse(this)
}
