package com.vdzon.administratie.model

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateDeserializer : JsonDeserializer<LocalDate>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(arg0: JsonParser, arg1: DeserializationContext): LocalDate {
        try {
            val text = arg0.text.replace("CET", "CEST") // oei! Dat is lelijk! Maar blijkbaar kan jackson niet goed omgaan met timezone afkortingen ?!
            //Tue Apr 05 00:00:00 CEST 2016
            val pattern = DateTimeFormatter.ofPattern("E MMM dd HH:m:ss 'CEST' yyyy")
            if (text.contains("CEST")) { // oei: Dit is al helemaal lelijk! We check op CEST om te kijken of het een date of een datetime is!
                return LocalDateTime.parse(text, pattern).toLocalDate()
            } else {
                return LocalDate.parse(arg0.text)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return LocalDate.now()
        }

    }

}