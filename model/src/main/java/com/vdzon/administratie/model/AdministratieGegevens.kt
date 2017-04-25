package com.vdzon.administratie.model

import org.litote.kmongo.MongoId

class AdministratieGegevens (
    @MongoId
    val uuid: String = "",
    val name: String = "",
    val rekeningNummer: String="",
    val btwNummer: String="",
    val handelsRegister: String="",
    val adres: String="",
    val postcode: String="",
    val woonplaats: String="",
    val logoUrl: String="") {

}