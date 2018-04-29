package com.vdzon.administratie.rest.checkandfix

import com.vdzon.administratie.checkandfix.CheckService
import com.vdzon.administratie.checkandfix.FixService
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import java.util.*
import javax.inject.Inject

class CheckAndFixService {

    @Inject
    lateinit internal var daoService: UserDao

    @Inject
    lateinit internal var checkService: CheckService

    @Inject
    lateinit internal var fixService: FixService

    fun getCheckAndFixRegels(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, daoService)
        return checkService.getCheckAndFixRegels(gebruiker.defaultAdministratie)
    }

    @Throws(Exception::class)
    fun fix(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, daoService)
        val updatedGebruiker = gebruiker.copy(administraties = Arrays.asList(fixService.getFixedAdministratie(gebruiker.defaultAdministratie)))
        daoService!!.updateGebruiker(updatedGebruiker)
        return SingleAnswer("ok")
    }


}
