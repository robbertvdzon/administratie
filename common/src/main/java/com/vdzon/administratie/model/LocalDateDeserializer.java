package com.vdzon.administratie.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;

public class LocalDateDeserializer extends JsonDeserializer<LocalDate> {
    @Override
    public LocalDate deserialize(JsonParser arg0, DeserializationContext arg1) throws IOException, JsonProcessingException {
        try {
            System.out.println(arg0.getText());
            //Tue Apr 05 00:00:00 CEST 2016

//            return LocalDateTime.parse(arg0.getText(), DateTimeFormatter.RFC_1123_DATE_TIME);
            return LocalDate.parse(arg0.getText());
        }
        catch(Exception ex){
            ex.printStackTrace();
            return LocalDate.now();
        }
    }
}