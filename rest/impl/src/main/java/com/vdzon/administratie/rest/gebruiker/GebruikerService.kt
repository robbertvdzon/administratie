package com.vdzon.administratie.rest.gebruiker

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.authenticatie.AuthenticationService
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.GebruikerDto
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import java.util.*
import javax.inject.Inject

class GebruikerService {

    @Inject
    lateinit internal var daoService: UserDao

    @Inject
    lateinit internal var athenticationService: AuthenticationService

    @Throws(Exception::class)
    fun postGebruiker(req: Request, res: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        val nieuweGebruikerJson = req.body()
        val mapper = jacksonObjectMapper()
        val nieuweGebruikerDto = mapper.readValue(nieuweGebruikerJson, GebruikerDto::class.java)
        var originalGebruiker: Gebruiker? = daoService!!.getGebruiker(nieuweGebruikerDto.uuid)
        if (originalGebruiker == null) {
            originalGebruiker = Gebruiker("", "", "", false, ArrayList<Administratie>())
        }
        val updatedGebruiker = nieuweGebruikerDto.cloneGebruikerWithDtoFields(originalGebruiker)
        daoService!!.updateGebruiker(updatedGebruiker)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun removeGebruiker(req: Request, res: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        var gebruikerUuid: String? = req.params(":uuid")
        if ("undefined" == gebruikerUuid) {
            gebruikerUuid = null
        }
        daoService!!.deleteGebruiker(gebruikerUuid)
        return SingleAnswer("ok")
    }

}
