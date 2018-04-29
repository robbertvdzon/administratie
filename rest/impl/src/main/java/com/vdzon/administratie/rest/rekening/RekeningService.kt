package com.vdzon.administratie.rest.rekening

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.RekeningDto
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.model.Rekening
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import javax.inject.Inject

class RekeningService {

    @Inject
    lateinit internal var daoService: UserDao

    @Throws(Exception::class)
    fun putRekening(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, daoService)
        val contactJson = req.body()
        var rekening: Rekening? = null
        val mapper = jacksonObjectMapper()
        val rekeningDto = mapper.readValue(contactJson, RekeningDto::class.java)
        rekening = rekeningDto.toRekening()

        gebruiker.defaultAdministratie.removeRekening(rekening.uuid)
        gebruiker.defaultAdministratie.addRekening(rekening)
        val boekingenVanRekening = BoekingenCache(gebruiker.defaultAdministratie.boekingen).getBoekingenVanRekening(rekening.rekeningNummer)
        for (boeking in boekingenVanRekening) {
            var found = false
            if (rekeningDto.boekingen != null) {
                for (boekingDto in rekeningDto.boekingen) {
                    if (boekingDto.uuid == boeking.uuid) {
                        found = true
                    }
                }
            }
            if (!found) {
                gebruiker.defaultAdministratie.removeBoeking(boeking.uuid)
            }
        }

        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun removeRekening(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, daoService)
        var rekeningUuid: String? = req.params(":uuid")
        if ("undefined" == rekeningUuid) {
            rekeningUuid = null
        }
        val huidigeRekening = gebruiker.defaultAdministratie.getRekening(rekeningUuid!!)
        removeBoekingenVanRekening(gebruiker, huidigeRekening!!.rekeningNummer)
        gebruiker.defaultAdministratie.removeRekening(rekeningUuid)
        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    private fun removeBoekingenVanRekening(gebruiker: Gebruiker, rekeningNr: String) {
        val defaultAdministratie = gebruiker.defaultAdministratie
        defaultAdministratie.boekingen
                .filter { boeking -> boeking is BoekingMetRekening }
                .map { boeking -> boeking as BoekingMetRekening }
                .filter { boeking -> boeking.rekeningNummer == rekeningNr }
                .forEach { boeking -> defaultAdministratie.removeBoeking(boeking.uuid) }
    }


}
