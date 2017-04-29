package com.vdzon.administratie.rest.overzicht

import com.vdzon.administratie.authenticatie.AuthenticationService
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.pdfgenerator.factuur.GenerateOverzicht
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import java.io.BufferedOutputStream
import javax.inject.Inject

class OverzichtService() {

    @Inject
    lateinit internal var generateOverzicht: GenerateOverzicht

    @Inject
    lateinit internal var daoService: UserDao

    @Inject
    lateinit internal var athenticationService: AuthenticationService

    @Throws(Exception::class)
    fun getPdf(req: Request, res: Response): Any? {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        val beginDate = req.params(":beginDate")
        val endDate = req.params(":endDate")
        if ("endDate" == endDate) {
            res.status(404)
            return SingleAnswer("not found")
        }
        if ("beginDate" == beginDate) {
            res.status(404)
            return SingleAnswer("not found")
        }

        res.raw().contentType = "application/pdf"
        res.raw().setHeader("Content-Disposition", "attachment; filename=overzicht_" + beginDate + "_" + endDate + ".pdf")
        BufferedOutputStream(res.raw().outputStream).use { zipOutputStream ->
            generateOverzicht.buildPdf(gebruiker.defaultAdministratie, beginDate, endDate, zipOutputStream)
            zipOutputStream.flush()
            zipOutputStream.close()
        }

        return null
    }

}
