package com.vdzon.administratie.rest.auth

import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response

import javax.inject.Inject
import javax.inject.Singleton
import java.util.ArrayList
import java.util.UUID

@Singleton
class AuthService {

    @Inject
    lateinit internal var userCrud: UserCrud

    @Throws(Exception::class)
    fun register(req: Request, res: Response): Any {
        val name = req.queryParams("name")
        val username = req.queryParams("username")
        val password = req.queryParams("password")

        var gebruiker: Gebruiker? = userCrud!!.getGebruikerByUsername(username)

        if (gebruiker != null) {
            res.status(401)
            SessionHelper.removeAuthenticatedUserUuid(req)
            return SingleAnswer("username bestaat al")
        }

        gebruiker = Gebruiker(
                UUID.randomUUID().toString(),
                name,
                username,
                password,
                false,
                ArrayList<Administratie>())
        gebruiker.initDefaultAdministratie()
        try {
            userCrud!!.updateGebruiker(gebruiker)
            SessionHelper.setAuthenticatedUserUuid(req, gebruiker.uuid!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun login(req: Request, res: Response): Any {
        val username = req.queryParams("username")
        val password = req.queryParams("password")
        val gebruiker = userCrud!!.getGebruikerByUsername(username)
        if (gebruiker == null || !gebruiker.authenticate(password)) {
            res.status(401)
            SessionHelper.removeAuthenticatedUserUuid(req)
            return SingleAnswer("not authorized")
        }
        SessionHelper.setAuthenticatedUserUuid(req, gebruiker.uuid!!)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun logout(req: Request, res: Response): Any {
        SessionHelper.removeAuthenticatedUserUuid(req)
        return SingleAnswer("ok")
    }

}
