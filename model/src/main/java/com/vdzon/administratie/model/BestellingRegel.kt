package com.vdzon.administratie.model

import org.litote.kmongo.MongoId
import java.io.Serializable

import java.math.BigDecimal

class BestellingRegel (
    @MongoId
    val uuid: String,
    val omschrijving: String,
    val aantal: BigDecimal,
    val stuksPrijs: BigDecimal,
    val btwPercentage: BigDecimal): Serializable {
}
