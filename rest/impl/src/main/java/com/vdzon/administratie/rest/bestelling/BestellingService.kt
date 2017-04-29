package com.vdzon.administratie.rest.bestelling

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.authenticatie.AuthenticationService
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.BestellingDto
import com.vdzon.administratie.model.Bestelling
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import javax.inject.Inject

class BestellingService {

    @Inject
    lateinit internal var daoService: UserDao

    @Inject
    lateinit internal var athenticationService: AuthenticationService


    @Throws(Exception::class)
    fun putBestelling(req: Request, res: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        val bestellingJson = req.body()
        val mapper = jacksonObjectMapper()
        val bestellingDto = mapper.readValue(bestellingJson, BestellingDto::class.java)
        val bestelling = bestellingDto.toBestelling()

        removeBestelling(gebruiker, bestelling.uuid)
        addBestelling(gebruiker, bestelling)

        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    private fun addBestelling(gebruiker: Gebruiker, bestelling: Bestelling) {
        val niewFactuurNummer = bestelling.gekoppeldFactuurNummer
        val gekoppelFactuur = gebruiker.defaultAdministratie.getFactuurByFactuurNummer(niewFactuurNummer)
        if (gekoppelFactuur != null) {
            val updatedFactuur = gekoppelFactuur.copy(gekoppeldeBestellingNummer = bestelling.bestellingNummer)
            gebruiker.defaultAdministratie.removeFactuur(gekoppelFactuur.uuid)
            gebruiker.defaultAdministratie.addFactuur(updatedFactuur!!)
        }
        gebruiker.defaultAdministratie.addBestelling(bestelling)
    }

    @Throws(Exception::class)
    fun removeBestelling(req: Request, res: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        var bestellingUuid: String? = req.params(":uuid")
        if ("undefined" == bestellingUuid) {
            bestellingUuid = null
        }
        removeBestelling(gebruiker, bestellingUuid)
        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    private fun removeBestelling(gebruiker: Gebruiker, uuid: String?) {
        val bestelling = gebruiker.defaultAdministratie.getBestelling(uuid)
        val factuurNummerOudeBestelling = bestelling?.gekoppeldFactuurNummer
        val gekoppelFactuur = gebruiker.defaultAdministratie.getFactuurByFactuurNummer(factuurNummerOudeBestelling!!)
        if (gekoppelFactuur != null) {
            val updatedFactuur = gekoppelFactuur.copy(gekoppeldeBestellingNummer = null)
            gebruiker.defaultAdministratie.removeFactuur(gekoppelFactuur.uuid)
            gebruiker.defaultAdministratie.addFactuur(updatedFactuur!!)
        }
        gebruiker.defaultAdministratie.removeBestelling(uuid)
    }

}
