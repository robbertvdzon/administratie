package com.vdzon.administratie.model

import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

@Entity("contact")
class Contact (
    @Id
    val uuid: String,
    val klantNummer: String,
    val naam: String,
    val tenNameVan: String,
    val woonplaats: String,
    val adres: String,
    val postcode: String,
    val land: String){
}
