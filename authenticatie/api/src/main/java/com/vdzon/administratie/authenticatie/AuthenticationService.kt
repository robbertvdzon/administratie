package com.vdzon.administratie.authenticatie

import com.vdzon.administratie.model.Gebruiker
import spark.Request
import spark.Response
import java.nio.file.Path

interface AuthenticationService {
    fun init()
    fun getGebruikerOrThowForbiddenException(req: Request, res: Response): Gebruiker
}