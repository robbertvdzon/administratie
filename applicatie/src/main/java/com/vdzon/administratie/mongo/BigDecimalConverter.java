package com.vdzon.administratie.mongo;

import org.mongodb.morphia.converters.SimpleValueConverter;
import org.mongodb.morphia.converters.TypeConverter;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.MappingException;

import java.math.BigDecimal;

public class BigDecimalConverter extends TypeConverter implements
        SimpleValueConverter {

    public BigDecimalConverter() {
        super(BigDecimal.class);
    }

    @Override
    protected boolean isSupported(Class<?> c, MappedField optionalExtraInfo) {
        return BigDecimal.class.isAssignableFrom(c);
    }

    @Override
    public Object encode(Object value, MappedField optionalExtraInfo) {
        if(value == null)
            return null;
        return value.toString();
    }

    @Override
    public Object decode(Class targetClass, Object fromDBObject, MappedField optionalExtraInfo) throws MappingException {
        if (fromDBObject == null) return null;
        return new BigDecimal(fromDBObject.toString());
    }

}