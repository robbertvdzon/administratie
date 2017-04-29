package com.vdzon.administratie.authenticatie

import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.rest.DemoConfigFactory
import com.vdzon.administratie.rest.DemoHttpActionAdapter
import com.vdzon.administratie.util.ForbiddenException
import com.vdzon.administratie.util.JsonUtil
import com.vdzon.administratie.util.SingleAnswer
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.profile.CommonProfile
import org.pac4j.core.profile.ProfileManager
import org.pac4j.sparkjava.CallbackRoute
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
        val config = DemoConfigFactory().build()
        config.addAuthorizer("admin", RequireAnyRoleAuthorizer<CommonProfile>("ROLE_ADMIN"))
        config.httpActionAdapter = DemoHttpActionAdapter()
        val callback = CallbackRoute(config)
        Spark.get("/callback", callback)
        Spark.post("/callback", callback)
        val facebookFilter = SecurityFilter(config, "FacebookClient")
        Spark.before("/facebook", facebookFilter)
        Spark.get("/facebook", Route { req, res -> getRedirect(req, res) }, JsonUtil.json())
    }

    private fun getEmail(request: Request, response: Response): String? {
        val context: SparkWebContext = SparkWebContext(request, response)
        val manager: ProfileManager<CommonProfile> = ProfileManager<CommonProfile>(context)
        val all = manager.getAll(true)
        if (all.isEmpty()) return null
        val commonProfile = all[0]
        println("email"+commonProfile.email)
        return commonProfile.email
    }


    override fun getGebruikerOrThowForbiddenException(req: Request, res: Response): Gebruiker {
        val email = getEmail(req, res)
        val gebruiker = daoService.getGebruikerByUsername(email) ?: throw ForbiddenException()
        return gebruiker

    }

    private fun getRedirect(request: Request, response: Response): String {
        response.status(201)
        response.redirect("/#/facturen")
        return "OK"
    }

    override fun removeAuthenticatedUserUuid(req: Request) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setAuthenticatedUserUuid(req: Request, uuid: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUploadedFile(request: Request): Path {
        try {
            val location = "image"
            val maxFileSize: Long = 100000000
            val maxRequestSize: Long = 100000000
            val fileSizeThreshold = 1024

            val multipartConfigElement = MultipartConfigElement(
                    location, maxFileSize, maxRequestSize, fileSizeThreshold)
            request.raw().setAttribute("org.eclipse.jetty.multipartConfig", multipartConfigElement)
            val filename = request.raw().getPart("file").submittedFileName
            val uploadedFile = request.raw().getPart("file")
            val out = Paths.get(filename)
            out.toFile().delete()
            uploadedFile.inputStream.use { `in` ->
                Files.copy(`in`, out)
                uploadedFile.delete()
            }
            return out
        } catch (e: IOException) {
            e.printStackTrace()
            throw RuntimeException(e)
        } catch (e: ServletException) {
            e.printStackTrace()
            throw RuntimeException(e)
        }

    }

}