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
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import javax.inject.Inject
import javax.servlet.MultipartConfigElement
import javax.servlet.ServletException

class AuthenticationServiceImpl : AuthenticationService {

    @Inject
    lateinit internal var daoService: UserDao


    override fun init() {
        val config = AuthConfigFactory().build()
        config.addAuthorizer("admin", RequireAnyRoleAuthorizer<CommonProfile>("ROLE_ADMIN"))
        val callback = CallbackRoute(config)
        Spark.get("/callback", callback)
        Spark.post("/callback", callback)
        val facebookFilter = SecurityFilter(config, "FacebookClient")
        Spark.before("/facebook", facebookFilter)
        Spark.get("/facebook", Route { req, res -> getRedirect(req, res) }, JsonUtil.json())

        val localLogout = LogoutRoute(config, "/#/login")
        localLogout.destroySession = true
        Spark.get("/facebooklogout", localLogout)

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


    override fun getGebruikerOrThowForbiddenException(req: Request, res: Response): Gebruiker {
        val email = getEmail(req, res)
        val gebruiker = daoService.getGebruikerByUsername(email) ?: throw ForbiddenException()
        return gebruiker

    }

    private fun getRedirect(request: Request, response: Response): String {
        response.status(201)
        response.redirect("/#/")
        return "OK"
    }


}