//package com.vdzon.administratie.mongo
//
//import org.mongodb.morphia.converters.SimpleValueConverter
//import org.mongodb.morphia.converters.TypeConverter
//import org.mongodb.morphia.mapping.MappedField
//import org.mongodb.morphia.mapping.MappingException
//
//import java.math.BigDecimal
//
//class BigDecimalConverter : TypeConverter(BigDecimal::class.java), SimpleValueConverter {
//
//    override fun isSupported(c: Class<*>?, optionalExtraInfo: MappedField?): Boolean {
//        return BigDecimal::class.java.isAssignableFrom(c!!)
//    }
//
//    override fun encode(value: Any?, optionalExtraInfo: MappedField?): Any? {
//        if (value == null)
//            return null
//        return value.toString()
//    }
//
//    @Throws(MappingException::class)
//    override fun decode(targetClass: Class<*>, fromDBObject: Any?, optionalExtraInfo: MappedField): Any? {
//        if (fromDBObject == null) return null
//        return BigDecimal(fromDBObject.toString())
//    }
//
//}