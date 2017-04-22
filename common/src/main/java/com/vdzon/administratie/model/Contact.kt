package com.vdzon.administratie.model

import org.litote.kmongo.MongoId

class Contact (
    @MongoId
    val uuid: String,
    val klantNummer: String,
    val naam: String,
    val tenNameVan: String?,
    val woonplaats: String,
    val adres: String,
    val postcode: String,
    val land: String?)
