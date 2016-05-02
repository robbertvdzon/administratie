package com.vdzon.administratie.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//
//public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
//    @Override
//    public LocalDate unmarshal(String dateInput) throws Exception {
//        return LocalDate.parse(dateInput, DateTimeFormatter.ISO_DATE);
//    }
//    @Override
//    public String marshal(LocalDate localDate) throws Exception {
//        return DateTimeFormatter.ISO_DATE.format(localDate);
//    }
//}

//public class LocalDateAdapter extends JsonSerializer<LocalDate> {
//
//    @Override
//    public void serialize(LocalDate value, JsonGenerator gen,
//                          SerializerProvider arg2){
//        gen.writeString(formatter.print(value));
//        gen.writeSs
//        return LocalDate.parse(dateInput, DateTimeFormatter.ISO_DATE);
//            throws IOException, JsonProcessingException {
//
//        gen.writeString(formatter.print(value));
//    }
//}