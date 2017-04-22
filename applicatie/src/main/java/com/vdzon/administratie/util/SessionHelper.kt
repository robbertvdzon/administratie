package com.vdzon.administratie.util

import com.vdzon.administratie.exceptions.ForbiddenException
import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.model.Gebruiker
import spark.Request
import spark.Session

import javax.servlet.MultipartConfigElement
import javax.servlet.ServletException
import javax.servlet.http.Part
import java.io.IOException
import java.io.InputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

object SessionHelper {

    private val AUTHENTICATED_USER_UUID = "authenticatedUserUuid"

    fun getAuthenticatedUserUuid(req: Request): String? {
        val session = req.session(true)
        val uuid = session.attribute<String>(AUTHENTICATED_USER_UUID)
        return uuid
    }

    fun setAuthenticatedUserUuid(req: Request, uuid: String) {
        val session = req.session(true)
        session.attribute(AUTHENTICATED_USER_UUID, uuid)
    }

    fun removeAuthenticatedUserUuid(req: Request) {
        val session = req.session(true)
        session.removeAttribute(AUTHENTICATED_USER_UUID)
    }

    fun getGebruikerOrThowForbiddenExceptin(req: Request, crudService: UserCrud): Gebruiker {
        val uuid = getAuthenticatedUserUuid(req)
        val gebruiker = crudService.getGebruiker(uuid) ?: throw ForbiddenException()
        return gebruiker
    }

    fun getUploadedFile(request: Request): Path {
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
