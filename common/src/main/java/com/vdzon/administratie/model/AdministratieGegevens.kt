package com.vdzon.administratie.model

import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Id

@Entity("administratieGegevens")
class AdministratieGegevens (
    @Id
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
