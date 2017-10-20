package com.vdzon.administratie.rest.afschrift

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.authenticatie.AuthenticationService
import com.vdzon.administratie.bankimport.ImportFromBank
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.AfschriftDto
import com.vdzon.administratie.model.Afschrift
import com.vdzon.administratie.model.BoekingenCache
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.model.boekingen.BetaaldeFactuurBoeking
import com.vdzon.administratie.model.boekingen.BetaaldeRekeningBoeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.inject.Inject
import javax.servlet.MultipartConfigElement
import javax.servlet.ServletException

class AfschriftService
@Inject
constructor(val importFromBank: ImportFromBank, var daoService: UserDao, var athenticationService: AuthenticationService) {

    @Throws(Exception::class)
    fun putAfschrift(req: Request, res: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        val afschriftJson = req.body()
        val mapper = jacksonObjectMapper()
        val afschriftDto = mapper.readValue(afschriftJson, AfschriftDto::class.java)
        val afschrift = afschriftDto.toAfschrift()

        gebruiker.defaultAdministratie.removeAfschrift(afschrift.nummer)
        gebruiker.defaultAdministratie.addAfschrift(afschrift)

        val boekingenVanAfschrift = BoekingenCache(gebruiker.defaultAdministratie.boekingen).getBoekingenVanAfschrift(afschrift.nummer)
        removeDeletedBoekingen(gebruiker, afschriftDto, boekingenVanAfschrift)
        addNewBoekingen(gebruiker, afschriftDto, boekingenVanAfschrift)

        daoService!!.updateGebruiker(gebruiker)
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
                            boekingDto.rekeningNummer!!,
                            afschriftDto.nummer,
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
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        var nummer: String? = req.params(":nummer")
        if ("undefined" == nummer) {
            nummer = null
        }
        gebruiker.defaultAdministratie.removeAfschrift(nummer!!)
        removeBoekingenVanAfschrift(gebruiker, nummer)
        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    private fun removeBoekingenVanAfschrift(gebruiker: Gebruiker, afschriftNummer: String) {
        val defaultAdministratie = gebruiker.defaultAdministratie
        defaultAdministratie.boekingen
                .filter({ boeking -> boeking is BoekingMetAfschrift })
                .map({ boeking -> boeking as BoekingMetAfschrift })
                .filter({ boeking -> boeking.afschriftNummer == afschriftNummer })
                .forEach { boeking -> defaultAdministratie.removeBoeking(boeking.uuid) }
    }

    fun uploadabn(request: Request, response: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(request, response)
        val uploadedFile = getUploadedFile(request)
        val afschriften = importFromBank.parseFile(uploadedFile, gebruiker)
        updateAfschriftenVanGebruiker(gebruiker, afschriften)
        daoService!!.updateGebruiker(gebruiker)
        return "OK"
    }


    fun getUploadedFile(request: Request): Path {
        try {
            val location = "image"
            val maxFileSize: Long = 100000000
            val maxRequestSize: Long = 100000000
            val fileSizeThreshold = 1024

            val multipartConfigElement = MultipartConfigElement(
                    location, maxFileSize, maxRequestSize, fileSizeThreshold)
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement)
            val filename = request.raw().getPart("file").submittedFileName
            val uploadedFile = request.raw().getPart("file")
            val out = Paths.get(filename)
            out.toFile().delete()
            uploadedFile.inputStream.use { `in` ->
                Files.copy(`in`, out)
                uploadedFile.delete()
            }
            return out
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException(e)
        } catch (e: ServletException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }

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
