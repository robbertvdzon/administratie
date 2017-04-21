package com.vdzon.administratie.model

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer

import java.io.IOException
import java.time.LocalDate

class LocalDateDeserializer : JsonDeserializer<LocalDate>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(arg0: JsonParser, arg1: DeserializationContext): LocalDate {
        try {
            println(arg0.text)
            //Tue Apr 05 00:00:00 CEST 2016

            //            return LocalDateTime.parse(arg0.getText(), DateTimeFormatter.RFC_1123_DATE_TIME);
            return LocalDate.parse(arg0.text)
        } catch (ex: Exception) {
            ex.printStackTrace()
            return LocalDate.now()
        }

    }
}