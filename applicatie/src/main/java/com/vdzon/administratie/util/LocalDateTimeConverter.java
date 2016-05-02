package com.vdzon.administratie.util;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

public class LocalDateTimeConverter extends TypeConverter implements SimpleValueConverter {

    public LocalDateTimeConverter() {
        // TODO: Add other date/time supported classes here
        // Other java.time classes: LocalDate.class, LocalTime.class
        // Arrays: LocalDateTime[].class, etc
        super(LocalDate.class);
    }

    @Override
    public Object decode(Class<?> targetClass, Object fromDBObject, MappedField optionalExtraInfo) {
        if (fromDBObject == null) {
            return null;
        }

        if (fromDBObject instanceof Date) {
            return ((Date) fromDBObject).toInstant().atZone(ZoneOffset.systemDefault()).toLocalDate();
        }

        if (fromDBObject instanceof LocalDate) {
            return fromDBObject;
        }

        // TODO: decode other types

        throw new IllegalArgumentException(String.format("Cannot decode object of class: %s", fromDBObject.getClass().getName()));
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if (value == null) {
            return null;
        }

        if (value instanceof Date) {
            return value;
        }

        if (value instanceof LocalDate) {
            LocalDate localDate = (LocalDate) value;
//            return Date.from(localDate.atStartOfDay().toInstant());

//            LocalDate localDate = (LocalDate)value;
//            ZonedDateTime zoned = ((LocalDate) value).atZone(ZoneOffset.systemDefault());
            return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
//            return Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.systemDefault());
        }

        // TODO: encode other types

        throw new IllegalArgumentException(String.format("Cannot encode object of class: %s", value.getClass().getName()));
    }
}
