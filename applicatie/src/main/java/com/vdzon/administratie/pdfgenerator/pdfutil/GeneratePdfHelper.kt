package com.vdzon.administratie.pdfgenerator.pdfutil

import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject

import java.io.*
import java.net.URL

class GeneratePdfHelper(private val pdfData: PdfData) {

    fun addAnImage(document: PDDocument, logoUrl: String) {
        try {
            saveLogoToFile(logoUrl, "logo.png")
            val ximage = PDImageXObject.createFromFile("logo.png", document)
            // TODO: waarom scale? Kan ik hier geen vast height gebruiken?
            val scale = 0.5f
            skipDown(ximage.height * scale)
            pdfData.page!!.drawImage(ximage, 30f, pdfData.pos, ximage.width * scale, ximage.height * scale)
        } catch (ioex: IOException) {
            println("No image for you")
        }

    }

    @Throws(IOException::class)
    private fun saveLogoToFile(logoUrl: String, filename: String) {
        val url = URL(logoUrl)
        val bufIn = BufferedInputStream(url.openStream())
        val out = ByteArrayOutputStream()
        val buf = ByteArray(1024)
        var n = 0
        while ({n = bufIn.read(buf);n}() != -1) {
            out.write(buf, 0, n)
        }

        out.close()
        bufIn.close()
        val response = out.toByteArray()
        val fos = FileOutputStream(filename)
        fos.write(response)
        fos.close()
    }

    @Throws(IOException::class)
    fun writeNormalText(text: String) {
        skipDown(NORMAL_TEXT_SKIPDOWN.toFloat())
        writeText(NORMAL_FONT_SIZE.toFloat(), pdfData.fontPlain, text)
    }

    @Throws(IOException::class)
    fun writeBoldText(text: String) {
        skipDown(NORMAL_TEXT_SKIPDOWN.toFloat())
        writeText(NORMAL_FONT_SIZE.toFloat(), pdfData.fontBold, text)
    }

    @Throws(IOException::class)
    fun writeTitle(text: String) {
        skipDown(TITLE_TEXT_SKIPDOWN.toFloat())
        writeText(TITLE_FONT_SIZE.toFloat(), pdfData.fontBold, text)
    }

    @Throws(IOException::class)
    fun writeText(fontSize: Float, fontPlain: PDFont, text: String) {
        writeText(fontSize, TEXT_POS_LEFT.toFloat(), fontPlain, text)
    }

    @Throws(IOException::class)
    fun writeText(fontSize: Float, x: Float, fontPlain: PDFont, text: String?) {
        if (text == null) return
        pdfData.page!!.beginText()
        pdfData.page!!.setFont(fontPlain, fontSize)
        pdfData.page!!.newLineAtOffset(x, pdfData.pos)
        pdfData.page!!.showText(text)
        pdfData.page!!.endText()
    }

    @Throws(IOException::class)
    fun writeTabel5(font: PDFont, cols: TabelCols, titel1: String, titel2: String, titel3: String, titel4: String, titel5: String) {
        skipDown(NORMAL_TEXT_SKIPDOWN.toFloat())
        writeText(NORMAL_FONT_SIZE.toFloat(), cols.getCol(0).toFloat(), font, titel1)
        writeText(NORMAL_FONT_SIZE.toFloat(), cols.getCol(1).toFloat(), font, titel2)
        writeText(NORMAL_FONT_SIZE.toFloat(), cols.getCol(2).toFloat(), font, titel3)
        writeText(NORMAL_FONT_SIZE.toFloat(), cols.getCol(3).toFloat(), font, titel4)
        writeText(NORMAL_FONT_SIZE.toFloat(), cols.getCol(4).toFloat(), font, titel5)
    }

    @Throws(IOException::class)
    fun writeTabel3(font: PDFont, cols: TabelCols, titel1: String, titel2: String, titel3: String) {
        skipDown(NORMAL_TEXT_SKIPDOWN.toFloat())
        writeText(NORMAL_FONT_SIZE.toFloat(), cols.getCol(0).toFloat(), font, titel1)
        writeText(NORMAL_FONT_SIZE.toFloat(), cols.getCol(1).toFloat(), font, titel2)
        writeText(NORMAL_FONT_SIZE.toFloat(), cols.getCol(2).toFloat(), font, titel3)
    }

    fun skipDown(amount: Float): Float {
        pdfData.pos -= amount
        return pdfData.pos
    }

    @Throws(IOException::class)
    fun drawLine() {
        skipDown(SKIPDOWN_LINE.toFloat())
        pdfData.page!!.setLineWidth(1f)
        pdfData.page!!.moveTo(0f, pdfData.pos)
        pdfData.page!!.lineTo(pdfData.pageWidth, pdfData.pos)
        pdfData.page!!.closeAndStroke()
    }

    companion object {
        val NORMAL_TEXT_SKIPDOWN = 15
        val TITLE_TEXT_SKIPDOWN = 20
        val TITLE_FONT_SIZE = 19
        val NORMAL_FONT_SIZE = 12
        val SKIPDOWN_LINE = 5
        val TEXT_POS_LEFT = 30
    }
}