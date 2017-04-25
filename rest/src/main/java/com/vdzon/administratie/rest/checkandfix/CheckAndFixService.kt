package com.vdzon.administratie.rest.checkandfix

import com.vdzon.administratie.checkandfix.CheckService
import com.vdzon.administratie.checkandfix.FixService

import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.model.*
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response

import javax.inject.Inject
import java.util.*

class CheckAndFixService {

    @Inject
    lateinit internal var crudService: UserCrud

    fun getCheckAndFixRegels(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        return CheckService.getCheckAndFixRegels(gebruiker.defaultAdministratie)
    }

    @Throws(Exception::class)
    fun fix(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
        val updatedGebruiker = gebruiker.copy(administraties = Arrays.asList(FixService.getFixedAdministratie(gebruiker.defaultAdministratie) ))
        crudService!!.updateGebruiker(updatedGebruiker)
        return SingleAnswer("ok")
    }


}
