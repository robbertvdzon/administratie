package com.vdzon.administratie.pdfgenerator.pdfutil

import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font

class PdfData {
    var fontPlain: PDFont = PDType1Font.HELVETICA
    var fontBold: PDFont = PDType1Font.HELVETICA_BOLD
    var pos = 0f
    var pageHeight = 0f
    var pageWidth = 0f
    var page: PDPageContentStream? = null

}
