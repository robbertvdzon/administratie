package com.vdzon.administratie.extensions

import java.time.format.DateTimeFormatter

val MY_DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy")


val ADMINISTRATIE_DATE_FORMATTER: DateTimeFormatter
    get() = MY_DATE_FORMATTER
