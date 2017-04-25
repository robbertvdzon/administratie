package com.vdzon.administratie.model.boekingen

import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="className")
abstract class Boeking {
    abstract val uuid: String
    abstract val omschrijving: String
}
