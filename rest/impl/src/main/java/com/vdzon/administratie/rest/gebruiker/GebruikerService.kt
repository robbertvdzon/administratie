package com.vdzon.administratie.rest.gebruiker

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.GebruikerDto
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response

import javax.inject.Inject
import java.util.ArrayList

class GebruikerService {

    @Inject
    lateinit internal var daoService: UserDao

    @Throws(Exception::class)
    fun postGebruiker(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, daoService)
        val nieuweGebruikerJson = req.body()
        val mapper = jacksonObjectMapper()
        val nieuweGebruikerDto = mapper.readValue(nieuweGebruikerJson, GebruikerDto::class.java)
        var originalGebruiker: Gebruiker? = daoService!!.getGebruiker(nieuweGebruikerDto.uuid)
        if (originalGebruiker == null) {
            originalGebruiker = Gebruiker("", "", "", "", false, ArrayList<Administratie>())
        }
        val updatedGebruiker = nieuweGebruikerDto.cloneGebruikerWithDtoFields(originalGebruiker)
        daoService!!.updateGebruiker(updatedGebruiker)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun removeGebruiker(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, daoService)
        var gebruikerUuid: String? = req.params(":uuid")
        if ("undefined" == gebruikerUuid) {
            gebruikerUuid = null
        }
        daoService!!.deleteGebruiker(gebruikerUuid)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun updatePassword(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, daoService)
        val gebruikerUuid = req.params(":uuid")
        val newPassword = req.params(":newPassword")
        val gebruikerToChange = daoService!!.getGebruiker(gebruikerUuid)
        val changedGebruiker = gebruikerToChange?.copy(password = newPassword)
        daoService!!.updateGebruiker(changedGebruiker)
        return SingleAnswer("ok")
    }

}
