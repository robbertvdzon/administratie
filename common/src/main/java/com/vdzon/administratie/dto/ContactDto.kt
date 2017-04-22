package com.vdzon.administratie.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.Contact
import java.util.*

@JsonIgnoreProperties
class ContactDto(
        val uuid: String = "",
        val klantNummer: String = "",
        val naam: String = "",
        val tenNameVan: String = "",
        val woonplaats: String = "",
        val adres: String = "",
        val postcode: String = "",
        val land: String = "") {

    fun toContact(): Contact = Contact(
                uuid=uuid,
                klantNummer=klantNummer,
                naam=naam,
                tenNameVan=tenNameVan,
                woonplaats=woonplaats,
                adres=adres,
                postcode=postcode,
                land=land)

    companion object {

        fun toDto(contact: Contact) = ContactDto(
                uuid = contact.uuid?:UUID.randomUUID().toString(),
                klantNummer = contact.klantNummer?:"",
                naam = contact.naam?:"",
                tenNameVan = contact.tenNameVan?:"",
                woonplaats = contact.woonplaats?:"",
                adres = contact.adres?:"",
                postcode = contact.postcode?:"",
                land = contact.land?:""
                )

    }
}
