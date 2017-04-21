package com.vdzon.administratie.rest.gebruiker

import com.fasterxml.jackson.databind.ObjectMapper
import com.vdzon.administratie.crud.UserCrud
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
    lateinit internal var crudService: UserCrud

    @Throws(Exception::class)
    fun postGebruiker(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        val nieuweGebruikerJson = req.body()
        val mapper = ObjectMapper()
        val nieuweGebruikerDto = mapper.readValue(nieuweGebruikerJson, GebruikerDto::class.java)
        var originalGebruiker: Gebruiker? = crudService!!.getGebruiker(nieuweGebruikerDto.uuid)
        if (originalGebruiker == null) {
            originalGebruiker = Gebruiker("", "", "", "", false, ArrayList<Administratie>())
        }
        val updatedGebruiker = nieuweGebruikerDto.cloneGebruikerWithDtoFields(originalGebruiker)
        crudService!!.updateGebruiker(updatedGebruiker)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun removeGebruiker(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        var gebruikerUuid: String? = req.params(":uuid")
        if ("undefined" == gebruikerUuid) {
            gebruikerUuid = null
        }
        crudService!!.deleteGebruiker(gebruikerUuid)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun updatePassword(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        val gebruikerUuid = req.params(":uuid")
        val newPassword = req.params(":newPassword")
        val gebruikerToChange = crudService!!.getGebruiker(gebruikerUuid)
        val changedGebruiker = gebruikerToChange?.copy(password = newPassword)
        crudService!!.updateGebruiker(changedGebruiker)
        return SingleAnswer("ok")
    }

}
