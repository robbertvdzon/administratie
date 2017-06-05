package com.vdzon.administratie.model

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate

internal class LocalDateDeserializerTest {
    @Test
    fun test_valid_dates(): Unit {
        testDate("2017-01-05", "2017-01-05")
        testDate("Tue Apr 05 00:00:00 GMT 2016", "2016-04-05")
        testDate("Tue Apr 05 00:00:00 CEST 2016", "2016-04-05")
        testDate("Tue Apr 05 00:00:00 CET 2016", "2016-04-05")
        testDate("Tue Apr 05 00:00:00 UTC 2016", "2016-04-05")
    }


    fun testDate(dateIn: String, dateOut: String): Unit {
        val json = "{\"value\":\"$dateIn\"}";
        val deserialisedNumber = deserialiseDate(json);
        assertEquals(dateOut, deserialisedNumber.toString());
    }

    fun deserialiseDate(json: String): LocalDate {
        val mapper = ObjectMapper();
        val deserializer = LocalDateDeserializer();
        val stream = ByteArrayInputStream(json.toByteArray(StandardCharsets.UTF_8));
        val parser = mapper.getFactory().createParser(stream);
        val ctxt = mapper.getDeserializationContext();
        parser.nextToken();
        parser.nextToken();
        parser.nextToken();
        return deserializer.deserialize(parser, ctxt);
    }

}