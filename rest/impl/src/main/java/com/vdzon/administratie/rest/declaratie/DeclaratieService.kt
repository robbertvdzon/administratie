package com.vdzon.administratie.rest.declaratie

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.DeclaratieDto
import com.vdzon.administratie.model.Declaratie
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response

import javax.inject.Inject

class DeclaratieService {

    @Inject
    lateinit internal var daoService: UserDao

    @Throws(Exception::class)
    fun putDeclaratie(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, daoService)
        val declaratieJson = req.body()
        var declaratie: Declaratie? = null
        val mapper = jacksonObjectMapper()
        val declaratieDto = mapper.readValue(declaratieJson, DeclaratieDto::class.java)
        declaratie = declaratieDto.toDeclaratie()

        gebruiker.defaultAdministratie.removeDeclaratie(declaratie.uuid)
        gebruiker.defaultAdministratie.addDeclaratie(declaratie)
        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun removeDeclaratie(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, daoService)
        var declaratieUuid: String? = req.params(":uuid")
        if ("undefined" == declaratieUuid) {
            declaratieUuid = null
        }
        gebruiker.defaultAdministratie.removeDeclaratie(declaratieUuid!!)
        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }
}
