package com.vdzon.administratie.model.boekingen

import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.io.Serializable

@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include= JsonTypeInfo.As.PROPERTY, property="className")
abstract class Boeking : Serializable {
    abstract val uuid: String
    abstract val omschrijving: String
}
