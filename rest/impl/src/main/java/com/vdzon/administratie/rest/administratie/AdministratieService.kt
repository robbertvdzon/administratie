package com.vdzon.administratie.rest.administratie

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.AdministratieGegevensDto
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.AdministratieGegevens
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import java.util.*

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdministratieService {

    @Inject
    lateinit internal var userDao: UserDao

    @Throws(Exception::class)
    fun putAdministratie(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, userDao)

        val administratieGegevensJson = req.body()
        val mapper = jacksonObjectMapper()
        val administratieGegevensDto = mapper.readValue(administratieGegevensJson, AdministratieGegevensDto::class.java)
        val administratieGegevens = administratieGegevensDto.toAdministratieGegevens()

        val administratie = gebruiker.defaultAdministratie.copy(administratieGegevens=administratieGegevens);
        val administraties = Arrays.asList(administratie);
        val gebruikerToUpdate = gebruiker.copy(administraties=administraties)
        userDao!!.updateGebruiker(gebruikerToUpdate);
        return SingleAnswer("ok")
    }
}
