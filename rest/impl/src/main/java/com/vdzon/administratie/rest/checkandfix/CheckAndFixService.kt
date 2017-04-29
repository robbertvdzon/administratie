package com.vdzon.administratie.rest.checkandfix

import com.vdzon.administratie.authenticatie.AuthenticationService
import com.vdzon.administratie.checkandfix.CheckService
import com.vdzon.administratie.checkandfix.FixService
import com.vdzon.administratie.database.UserDao
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

    @Inject
    lateinit internal var athenticationService: AuthenticationService


    fun getCheckAndFixRegels(req: Request, res: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        return checkService.getCheckAndFixRegels(gebruiker.defaultAdministratie)
    }

    @Throws(Exception::class)
    fun fix(req: Request, res: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        val updatedGebruiker = gebruiker.copy(administraties = Arrays.asList(fixService.getFixedAdministratie(gebruiker.defaultAdministratie)))
        daoService!!.updateGebruiker(updatedGebruiker)
        return SingleAnswer("ok")
    }


}
