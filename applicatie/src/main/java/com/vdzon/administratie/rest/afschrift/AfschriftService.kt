package com.vdzon.administratie.rest.afschrift

import com.fasterxml.jackson.databind.ObjectMapper
import com.vdzon.administratie.bankimport.ImportFromAbnAmro
import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.dto.AfschriftDto
import com.vdzon.administratie.dto.BoekingDto
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.model.boekingen.BetaaldeFactuurBoeking
import com.vdzon.administratie.model.boekingen.BetaaldeRekeningBoeking
import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response

import javax.inject.Inject
import java.nio.file.Path
import java.util.UUID

class AfschriftService {

    @Inject
    lateinit internal var crudService: UserCrud

    @Throws(Exception::class)
    fun putAfschrift(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        val afschriftJson = req.body()
        val mapper = ObjectMapper()
        val afschriftDto = mapper.readValue(afschriftJson, AfschriftDto::class.java)
        val afschrift = afschriftDto.toAfschrift()

        gebruiker.defaultAdministratie.removeAfschrift(afschrift.nummer)
        gebruiker.defaultAdministratie.addAfschrift(afschrift)

        val boekingenVanAfschrift = BoekingenCache(gebruiker.defaultAdministratie.boekingen).getBoekingenVanAfschrift(afschrift.nummer)
        removeDeletedBoekingen(gebruiker, afschriftDto, boekingenVanAfschrift)
        addNewBoekingen(gebruiker, afschriftDto, boekingenVanAfschrift)

        crudService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    private fun removeDeletedBoekingen(gebruiker: Gebruiker, afschriftDto: AfschriftDto, boekingenVanAfschrift: List<BoekingMetAfschrift>) {
        for (boeking in boekingenVanAfschrift) {
            var found = false
            for (boekingDto in afschriftDto.boekingen!!) {
                if (boekingDto.uuid == boeking.uuid) {
                    found = true
                }
            }
            if (!found) {
                gebruiker.defaultAdministratie.removeBoeking(boeking.uuid)
            }
        }
    }

    private fun addNewBoekingen(gebruiker: Gebruiker, afschriftDto: AfschriftDto, boekingenVanAfschrift: List<BoekingMetAfschrift>) {
        for (boekingDto in afschriftDto.boekingen!!) {
            var found = false
            for (boeking in boekingenVanAfschrift) {
                if (boekingDto.uuid == boeking.uuid) {
                    found = true
                }
            }
            if (!found) {
                if (notEmpty(boekingDto.factuurNummer)) {
                    val boeking = BetaaldeFactuurBoeking(
                            boekingDto.factuurNummer!!,
                            afschriftDto.nummer,
                            UUID.randomUUID().toString()
                    )
                    gebruiker.defaultAdministratie.addBoeking(boeking)
                } else if (notEmpty(boekingDto.rekeningNummer)) {
                    val boeking = BetaaldeRekeningBoeking(
                            afschriftDto.nummer,
                            boekingDto.rekeningNummer!!,
                            UUID.randomUUID().toString())
                    gebruiker.defaultAdministratie.addBoeking(boeking)
                }
            }
        }
    }

    private fun notEmpty(s: String?): Boolean {
        return s != null && s !== ""
    }

    @Throws(Exception::class)
    fun removeAfschrift(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        var nummer: String? = req.params(":nummer")
        if ("undefined" == nummer) {
            nummer = null
        }
        gebruiker.defaultAdministratie.removeAfschrift(nummer!!)
        removeBoekingenVanAfschrift(gebruiker, nummer)
        crudService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    private fun removeBoekingenVanAfschrift(gebruiker: Gebruiker, afschriftNummer: String) {
        val defaultAdministratie = gebruiker.defaultAdministratie
        defaultAdministratie.boekingen
                .filter({ boeking -> boeking is BoekingMetAfschrift })
                .map({ boeking -> boeking as BoekingMetAfschrift})
                .filter({ boeking -> boeking.afschriftNummer == afschriftNummer })
                .forEach { boeking -> defaultAdministratie.removeBoeking(boeking.uuid) }
    }

    fun uploadabn(request: Request, response: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(request, crudService)
        val uploadedFile = SessionHelper.getUploadedFile(request)
        val afschriften = ImportFromAbnAmro().parseFile(uploadedFile, gebruiker)
        updateAfschriftenVanGebruiker(gebruiker, afschriften)
        crudService!!.updateGebruiker(gebruiker)
        return "OK"
    }


    private fun updateAfschriftenVanGebruiker(gebruiker: Gebruiker, afschriften: List<Afschrift>) {
        for (afschrift in afschriften) {
            if (afschrift != null) {
                gebruiker.defaultAdministratie.removeAfschrift(afschrift.nummer)
                gebruiker.defaultAdministratie.addAfschrift(afschrift)
            }
        }
    }
}
