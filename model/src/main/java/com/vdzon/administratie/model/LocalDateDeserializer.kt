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
            // niet mooi, maar eerst parsen als een date. Als dat niet lukt, kijken of het een timestamp is en die parsen.
            try{
                return LocalDate.parse(arg0.text)
            }
            catch(ex:java.time.format.DateTimeParseException){
                val text = arg0.text
                        .replace("CET", "") // oei! Dat is lelijk! Maar blijkbaar kan jackson niet goed omgaan met timezone afkortingen ?!
                        .replace("CEST","")
                        .replace("UTC","")
                        .replace("GMT","")
                val pattern = DateTimeFormatter.ofPattern("E MMM dd HH:m:ss  yyyy")
                return LocalDateTime.parse(text, pattern).toLocalDate()
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
            return LocalDate.now()
        }

    }

}