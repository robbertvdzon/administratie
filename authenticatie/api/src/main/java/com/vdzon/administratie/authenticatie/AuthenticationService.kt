package com.vdzon.administratie.authenticatie

import com.vdzon.administratie.model.Gebruiker
import spark.Request
import spark.Response
import java.nio.file.Path

interface AuthenticationService {
    fun init()
    fun getGebruikerOrThowForbiddenException(req: Request, res: Response): Gebruiker
    fun removeAuthenticatedUserUuid(req: Request): Unit
    fun setAuthenticatedUserUuid(req: Request, uuid: String): Unit
    fun getUploadedFile(request: Request): Path
}