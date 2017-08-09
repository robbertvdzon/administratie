package com.vdzon.administratie.authenticatie

import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.rest.AuthConfigFactory
import com.vdzon.administratie.util.ForbiddenException
import com.vdzon.administratie.util.JsonUtil
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.profile.CommonProfile
import org.pac4j.core.profile.ProfileManager
import org.pac4j.sparkjava.CallbackRoute
import org.pac4j.sparkjava.LogoutRoute
import org.pac4j.sparkjava.SecurityFilter
import org.pac4j.sparkjava.SparkWebContext
import spark.Request
import spark.Response
import spark.Route
import spark.Spark
import javax.inject.Inject

class AuthenticationServiceImpl : AuthenticationService {

    @Inject
    lateinit internal var daoService: UserDao

    override fun init() {
        val config = AuthConfigFactory().build()
        config.addAuthorizer("admin", RequireAnyRoleAuthorizer<CommonProfile>("ROLE_ADMIN"))
        val callback = CallbackRoute(config)
        val facebookFilter = SecurityFilter(config, "FacebookClient")
        val localLogout = LogoutRoute(config, "/#/login")
        localLogout.destroySession = true

        Spark.post("/callback", callback)
        Spark.before("/facebook", facebookFilter)

        Spark.get("/callback", callback)
        Spark.get("/facebook", Route { req, res -> getRedirect(req, res) }, JsonUtil.json())
        Spark.get("/facebooklogout", localLogout)

    }

    override fun getGebruikerOrThowForbiddenException(req: Request, res: Response): Gebruiker {
        val email = getEmail(req, res)
        val gebruiker = daoService.getGebruikerByUsername(email)
        if (gebruiker==null ) {
            if (!email.isNullOrBlank()) {
                val newGebruiker = Gebruiker(name = "", isAdmin = false, username = email)
                daoService.addGebruiker(newGebruiker)
                return newGebruiker
            } else {
                throw ForbiddenException()
            }
        }

        return gebruiker

    }

    private fun getEmail(request: Request, response: Response): String? {
        val context: SparkWebContext = SparkWebContext(request, response)
        val manager: ProfileManager<CommonProfile> = ProfileManager<CommonProfile>(context)
        val all = manager.getAll(true)
        if (all.isEmpty()) return null
        val commonProfile = all[0]
        println("email" + commonProfile.email)
        return commonProfile.email
    }


    private fun getRedirect(request: Request, response: Response): String {
        response.status(201)
        response.redirect("/#/")
        return "OK"
    }


}