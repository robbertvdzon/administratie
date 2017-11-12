package com.vdzon.administratie.dto


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.vdzon.administratie.model.AdministratieGegevens
import java.util.*


@JsonIgnoreProperties
class AdministratieGegevensDto(
        val uuid: String,
        var name: String = "",
        var rekeningNummer: String = "",
        var rekeningNaam: String = "",
        var btwNummer: String = "",
        var handelsRegister: String = "",
        var adres: String = "",
        var postcode: String = "",
        var woonplaats: String = "",
        var logoUrl: String = "") {

    fun toAdministratieGegevens(): AdministratieGegevens = AdministratieGegevens(
            uuid = uuid,
            name = name,
            rekeningNummer = rekeningNummer,
            rekeningNaam = rekeningNaam,
            btwNummer = btwNummer,
            handelsRegister = handelsRegister,
            adres = adres,
            postcode = postcode,
            woonplaats = woonplaats,
            logoUrl = logoUrl)


    companion object {
        fun toDto(administratieGegegens: AdministratieGegevens?): AdministratieGegevensDto {
            if (administratieGegegens != null) {
                return AdministratieGegevensDto(
                        uuid = administratieGegegens.uuid,
                        name = administratieGegegens.name,
                        rekeningNummer = administratieGegegens.rekeningNummer,
                        rekeningNaam = administratieGegegens.rekeningNaam,
                        btwNummer = administratieGegegens.btwNummer,
                        handelsRegister = administratieGegegens.handelsRegister,
                        adres = administratieGegegens.adres,
                        postcode = administratieGegegens.postcode,
                        woonplaats = administratieGegegens.woonplaats,
                        logoUrl = administratieGegegens.logoUrl)
            } else {
                return AdministratieGegevensDto(
                        uuid = UUID.randomUUID().toString());
            }
        }
    }
}
