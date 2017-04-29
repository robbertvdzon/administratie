package com.vdzon.administratie.rest.auth

import com.vdzon.administratie.authenticatie.AuthenticationService
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthService {

    @Inject
    lateinit internal var userDao: UserDao

    @Inject
    lateinit internal var athenticationService: AuthenticationService

    @Throws(Exception::class)
    fun register(req: Request, res: Response): Any {
        val name = req.queryParams("name")
        val username = req.queryParams("username")
        val password = req.queryParams("password")

        var gebruiker: Gebruiker? = userDao.getGebruikerByUsername(username)

        if (gebruiker != null) {
            res.status(401)
            athenticationService.removeAuthenticatedUserUuid(req)
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
            userDao.updateGebruiker(gebruiker)
            athenticationService.setAuthenticatedUserUuid(req, gebruiker.uuid!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun login(req: Request, res: Response): Any {
        val username = req.queryParams("username")
        val password = req.queryParams("password")
        val gebruiker = userDao!!.getGebruikerByUsername(username)
        if (gebruiker == null || !gebruiker.authenticate(password)) {
            res.status(401)
            athenticationService.removeAuthenticatedUserUuid(req)
            return SingleAnswer("not authorized")
        }
        athenticationService.setAuthenticatedUserUuid(req, gebruiker.uuid!!)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun logout(req: Request, res: Response): Any {
        athenticationService.removeAuthenticatedUserUuid(req)
        return SingleAnswer("ok")
    }

}
