package com.vdzon.administratie.rest.overzicht

import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.model.Gebruiker
import com.vdzon.administratie.pdfgenerator.overzicht.GenerateOverzicht
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response

import javax.inject.Inject
import java.io.BufferedOutputStream

class OverzichtService {

    @Inject
    lateinit internal var crudService: UserCrud

    @Throws(Exception::class)
    fun getPdf(req: Request, res: Response): Any? {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService)
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
            GenerateOverzicht.buildPdf(gebruiker.defaultAdministratie, beginDate, endDate, zipOutputStream)
            zipOutputStream.flush()
            zipOutputStream.close()
        }

        return null
    }

}
