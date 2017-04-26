package com.vdzon.administratie.pdfgenerator.factuur

import com.vdzon.administratie.model.Administratie
import java.io.BufferedOutputStream
import java.io.IOException

interface GenerateOverzicht {

    @Throws(IOException::class)
    fun buildPdf(administratie: Administratie, beginDate: String, endDate: String, outputStream: BufferedOutputStream)

}