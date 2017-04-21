package com.vdzon.administratie.util

import org.mongodb.morphia.converters.SimpleValueConverter
import org.mongodb.morphia.converters.TypeConverter
import org.mongodb.morphia.mapping.MappedField

import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.Date

class LocalDateTimeConverter : TypeConverter(LocalDate::class.java), SimpleValueConverter {

    override fun decode(targetClass: Class<*>, fromDBObject: Any?, optionalExtraInfo: MappedField): Any? {
        if (fromDBObject == null) {
            return null
        }

        if (fromDBObject is Date) {
            return fromDBObject.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDate()
        }

        if (fromDBObject is LocalDate) {
            return fromDBObject
        }

        throw IllegalArgumentException(String.format("Cannot decode object of class: %s", fromDBObject.javaClass.name))
    }

    override fun encode(value: Any?, optionalExtraInfo: MappedField?): Any? {
        if (value == null) {
            return null
        }

        if (value is Date) {
            return value
        }

        if (value is LocalDate) {
            return Date.from(value.atStartOfDay(ZoneId.systemDefault()).toInstant())
        }

        throw IllegalArgumentException(String.format("Cannot encode object of class: %s", value.javaClass.name))
    }
}
