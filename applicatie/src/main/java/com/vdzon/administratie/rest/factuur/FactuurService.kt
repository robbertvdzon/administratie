package com.vdzon.administratie.rest.factuur

import com.fasterxml.jackson.databind.ObjectMapper
import com.vdzon.administratie.dto.BoekingDto
import com.vdzon.administratie.model.*
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.dto.FactuurDto
import com.vdzon.administratie.pdfgenerator.factuur.GenerateFactuur
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response

import javax.inject.Inject
import java.io.BufferedOutputStream

class FactuurService {

    @Inject
    lateinit internal var crudService: UserCrud

    @Throws(Exception::class)
    fun putFactuur(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        val factuurJson = req.body()
        var factuur: Factuur? = null
        val mapper = ObjectMapper()
        val factuurDto = mapper.readValue(factuurJson, FactuurDto::class.java)
        factuur = factuurDto.toFactuur()

        removeFactuur(gebruiker, factuur.uuid)
        addFactuur(gebruiker, factuur)

        val boekingenVanFactuur = BoekingenCache(gebruiker.defaultAdministratie.boekingen).getBoekingenVanFactuur(factuur.factuurNummer)
        for (boeking in boekingenVanFactuur) {
            var found = false
            for (boekingDto in factuurDto.boekingen) {
                if (boekingDto.uuid == boeking.uuid) {
                    found = true
                }
            }
            if (!found) {
                gebruiker.defaultAdministratie.removeBoeking(boeking.uuid)
            }
        }

        crudService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun removeFactuur(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        var factuurUuid: String? = req.params(":uuid")
        if ("undefined" == factuurUuid) {
            factuurUuid = null
        }
        val factuur = gebruiker.defaultAdministratie.getFactuur(factuurUuid!!)

        gebruiker.defaultAdministratie.removeFactuur(factuurUuid)
        removeBoekingenVanFactuur(gebruiker, factuur!!.factuurNummer)

        crudService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }


    private fun addFactuur(gebruiker: Gebruiker, factuur: Factuur) {
        val nieuwBestellingNummer = factuur.gekoppeldeBestellingNummer
        val gekoppelBestelling = gebruiker.defaultAdministratie.getBestellingByBestellingNummer(nieuwBestellingNummer!!)
        if (gekoppelBestelling != null) {
            val updatedBestelling = gekoppelBestelling.copy(gekoppeldFactuurNummer = factuur.factuurNummer)
            gebruiker.defaultAdministratie.removeBestelling(gekoppelBestelling.uuid)
            gebruiker.defaultAdministratie.addBestelling(updatedBestelling!!)
        }
        gebruiker.defaultAdministratie.addFactuur(factuur)
    }

    private fun removeFactuur(gebruiker: Gebruiker, uuid: String) {
        val factuur = gebruiker.defaultAdministratie.getFactuur(uuid)
        val bestellingNummerOudeFactuur = factuur?.gekoppeldeBestellingNummer
        val gekoppelBestelling = gebruiker.defaultAdministratie.getBestellingByBestellingNummer(bestellingNummerOudeFactuur!!)
        if (gekoppelBestelling != null) {
            val updatedBestelling = gekoppelBestelling.copy(gekoppeldFactuurNummer = null)
            gebruiker.defaultAdministratie.removeBestelling(gekoppelBestelling.uuid)
            gebruiker.defaultAdministratie.addBestelling(updatedBestelling!!)
        }
        gebruiker.defaultAdministratie.removeFactuur(uuid)
    }

    private fun removeBoekingenVanFactuur(gebruiker: Gebruiker, factuurNr: String) {
        val defaultAdministratie = gebruiker.defaultAdministratie
        defaultAdministratie.boekingen
                .filter{ boeking -> boeking is BoekingMetFactuur }
                .map{ boeking -> boeking  as BoekingMetFactuur}
                .filter{ boeking -> boeking.factuurNummer == factuurNr }
                .forEach { boeking -> defaultAdministratie.removeBoeking(boeking.uuid) }
    }

    @Throws(Exception::class)
    fun getPdf(req: Request, res: Response): Any? {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        val factuurUuid = req.params(":uuid")
        if ("undefined" == factuurUuid) {
            res.status(404)
            return SingleAnswer("not found")
        }

        val factuur = gebruiker.defaultAdministratie.getFactuur(factuurUuid)
        res.raw().contentType = "application/pdf"
        res.raw().setHeader("Content-Disposition", "attachment; filename=" + factuur!!.factuurNummer + ".pdf")
        BufferedOutputStream(res.raw().outputStream).use { zipOutputStream ->
            GenerateFactuur.buildPdf(gebruiker.defaultAdministratie, factuur, zipOutputStream)
            zipOutputStream.flush()
            zipOutputStream.close()
        }
        return null
    }

}
