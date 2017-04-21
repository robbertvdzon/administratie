package com.vdzon.administratie.model

import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

import java.math.BigDecimal

@Entity("bestellingRegel")
class BestellingRegel (
    @Id
    val uuid: String,
    val omschrijving: String,
    val aantal: BigDecimal,
    val stuksPrijs: BigDecimal,
    val btwPercentage: BigDecimal){
}
