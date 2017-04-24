package com.vdzon.administratie.pdfgenerator.factuur

import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.Factuur
import java.io.BufferedOutputStream
import java.io.IOException

interface GenerateFactuur{

    @Throws(IOException::class)
    fun buildPdf(administratie: Administratie, factuur: Factuur, outputStream: BufferedOutputStream)

}