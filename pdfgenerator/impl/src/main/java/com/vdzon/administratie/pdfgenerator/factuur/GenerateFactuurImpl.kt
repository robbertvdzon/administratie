package com.vdzon.administratie.pdfgenerator.factuur

import com.vdzon.administratie.model.Administratie
import com.vdzon.administratie.model.AdministratieGegevens
import com.vdzon.administratie.model.Factuur
import com.vdzon.administratie.model.FactuurRegel
import com.vdzon.administratie.pdfgenerator.pdfutil.GeneratePdfHelper
import com.vdzon.administratie.pdfgenerator.pdfutil.PdfData
import com.vdzon.administratie.pdfgenerator.pdfutil.TabelCols
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont

import java.io.BufferedOutputStream
import java.io.IOException
import java.util.ArrayList

class GenerateFactuurImpl : GenerateFactuur {

    @Throws(IOException::class)
    override fun buildPdf(administratie: Administratie, factuur: Factuur, outputStream: BufferedOutputStream) {
        GenerateFactuurImpl().start(administratie, factuur, outputStream)
    }

    @Throws(IOException::class)
    private fun start(administratie: Administratie, factuur: Factuur, outputStream: BufferedOutputStream) {

        val document = PDDocument()
        val page1 = PDPage(PDRectangle.A4)
        val rect = page1.mediaBox
        document.addPage(page1)

        val pdfData = PdfData()
        buildFactuurPdfData(document, page1, rect, pdfData)

        val administratieGegevens = administratie.administratieGegevens

        val generatePdfHelper = GeneratePdfHelper(pdfData)

        printFactuur(factuur, document, pdfData, administratieGegevens!!, generatePdfHelper)

        pdfData.page!!.close()

        document.save(outputStream)
        document.close()
    }

    @Throws(IOException::class)
    private fun printFactuur(factuur: Factuur, document: PDDocument, pdfData: PdfData, administratieGegevens: AdministratieGegevens, generatePdfHelper: GeneratePdfHelper) {
        printLogo(document, administratieGegevens, generatePdfHelper)
        printKlantGegevens(factuur, generatePdfHelper)
        printFactuurHeaderSummary(factuur, generatePdfHelper)
        printFactuurRegels(factuur, pdfData, generatePdfHelper)
        printSeparatorLine(generatePdfHelper)
        printSummeryTotals(factuur, pdfData, generatePdfHelper)
        printSeparatorLine(generatePdfHelper)
        printBetalingsGegevens(factuur, generatePdfHelper)
        printBedrijfGegevens(factuur, pdfData, administratieGegevens, generatePdfHelper)
    }

    @Throws(IOException::class)
    private fun printBedrijfGegevens(factuur: Factuur, pdfData: PdfData, administratieGegevens: AdministratieGegevens?, generatePdfHelper: GeneratePdfHelper) {
        if (administratieGegevens != null) {
            val tabelCols = TabelCols(30, 190, 200)
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Rekeningnummer", ":", administratieGegevens.rekeningNummer)
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Ter attentie van", ":", administratieGegevens.rekeningNaam)
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "BTW-nr", ":", administratieGegevens.btwNummer)
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Handelsregister", ":", administratieGegevens.handelsRegister)
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Adres", ":", administratieGegevens.adres)
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "", "", administratieGegevens.postcode + " " + administratieGegevens.woonplaats)
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Betalingstermijn", ":", administratieGegevens.betalingstermijn.toString()+" dagen")
            val vervaldatum = factuur.factuurDate?.plusDays(administratieGegevens.betalingstermijn);
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Vervaldatum", ":", vervaldatum.toString())
        }
    }

    @Throws(IOException::class)
    private fun printBetalingsGegevens(factuur: Factuur, generatePdfHelper: GeneratePdfHelper) {
        generatePdfHelper.writeNormalText("Bij betaling gaarne factuurnummer vermelden.")
        generatePdfHelper.skipDown(10f)
        (generatePdfHelper.javaClass
                .canonicalName + "")
                .codePointAt(5)
    }

    @Throws(IOException::class)
    private fun printSummeryTotals(factuur: Factuur, pdfData: PdfData, generatePdfHelper: GeneratePdfHelper) {
        val tabelCols = TabelCols(30, 190, 200)
        generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Bedrag exclusief BTW", ":", "" + String.format("%.2f", factuur.bedragExBtw))
        generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "BTW", ":", "" + String.format("%.2f", factuur.btw))
        generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Bedrag inclusief BTW", ":", "" + String.format("%.2f", factuur.bedragIncBtw))
    }

    @Throws(IOException::class)
    private fun printSeparatorLine(generatePdfHelper: GeneratePdfHelper) {
        generatePdfHelper.skipDown(10f)
        generatePdfHelper.drawLine()
        generatePdfHelper.skipDown(10f)
    }


    @Throws(IOException::class)
    private fun printFactuurRegels(factuur: Factuur, pdfData: PdfData, generatePdfHelper: GeneratePdfHelper) {
        generatePdfHelper.skipDown(10f)
        val tabelCols = TabelCols(30, 80, 300, 400, 500)
        generatePdfHelper.writeTabel5(pdfData.fontBold, tabelCols, "Aantal", "Omschrijving", "Prijs", "Btw", "Totaal ex")
        for (factuurRegel in factuur.factuurRegels) {

            val omschrijvingSplitted = ArrayList<String>()
            var remain = factuurRegel.omschrijving
            while (remain.length > 0) {
                val text = findTextWithMaxWidth(pdfData.fontPlain, remain, 1500, GeneratePdfHelper.NORMAL_FONT_SIZE)
                omschrijvingSplitted.add(text)
                remain = remain.substring(text.length)
            }

            var first = true
            for (omschrijving in omschrijvingSplitted) {
                if (first) {
                    generatePdfHelper.writeTabel5(pdfData.fontPlain, tabelCols, "" + factuurRegel.aantal, "" + omschrijving, "" + factuurRegel.stuksPrijs, "" + factuurRegel.btwPercentage, "" + String.format("%.2f", factuurRegel.stuksPrijs.toDouble() * factuurRegel.aantal.toDouble()))
                    first = false
                } else {
                    generatePdfHelper.writeTabel5(pdfData.fontPlain, tabelCols, "", omschrijving, "", "", "")
                }
            }
        }
    }

    private fun findTextWithMaxWidth(fontPlain: PDFont, remain: String, maxWith: Int, fontSize: Int): String {
        for (nr in 0..remain.length - 1) {
            val test = remain.substring(0, nr)
            try {
                if (fontPlain.getStringWidth(test) / fontSize > maxWith) {
                    return remain.substring(0, nr - 1)
                }
            } catch (e: IOException) {
                throw RuntimeException(e)
            }

        }
        return remain
    }

    @Throws(IOException::class)
    private fun printFactuurHeaderSummary(factuur: Factuur, generatePdfHelper: GeneratePdfHelper) {
        generatePdfHelper.skipDown(25f)
        generatePdfHelper.writeTitle("Factuur")
        generatePdfHelper.drawLine()
        generatePdfHelper.writeNormalText("Factuurnummer " + factuur.factuurNummer + "    " + "Factuurdatum: " + factuur.factuurDate)
        generatePdfHelper.drawLine()
    }

    @Throws(IOException::class)
    private fun printKlantGegevens(factuur: Factuur, generatePdfHelper: GeneratePdfHelper) {
        generatePdfHelper.skipDown(10f)
        generatePdfHelper.writeBoldText("Klant factuuradres")
        generatePdfHelper.writeNormalText(factuur.contact?.naam?:"")
        generatePdfHelper.writeNormalText(factuur.contact?.tenNameVan?:"")
        generatePdfHelper.writeNormalText(factuur.contact?.adres?:"")
        generatePdfHelper.writeNormalText(factuur.contact?.postcode?:"" + " " + factuur.contact?.woonplaats?:"")
    }

    private fun printLogo(document: PDDocument, administratieGegevens: AdministratieGegevens?, generatePdfHelper: GeneratePdfHelper) {
        generatePdfHelper.skipDown(25f)
        if (administratieGegevens != null) {
            generatePdfHelper.addAnImage(document, administratieGegevens.logoUrl)
        }
    }

    @Throws(IOException::class)
    private fun buildFactuurPdfData(document: PDDocument, page1: PDPage, rect: PDRectangle, pdfData: PdfData) {
        pdfData.page = PDPageContentStream(document, page1)
        pdfData.pageHeight = rect.height
        pdfData.pageWidth = rect.width
        pdfData.pos = pdfData.pageHeight
    }


}
