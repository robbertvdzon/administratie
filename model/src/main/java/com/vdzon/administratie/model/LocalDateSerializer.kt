package com.vdzon.administratie.model

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider

import java.io.IOException
import java.time.LocalDate

class LocalDateSerializer : JsonSerializer<LocalDate>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun serialize(arg0: LocalDate, arg1: JsonGenerator, arg2: SerializerProvider) {
        try {
            arg1.writeString(arg0.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }
}

