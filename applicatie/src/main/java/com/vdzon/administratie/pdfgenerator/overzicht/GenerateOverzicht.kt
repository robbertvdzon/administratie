package com.vdzon.administratie.pdfgenerator.overzicht

import com.vdzon.administratie.checkandfix.CheckService
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel2

import com.vdzon.administratie.model.boekingen.Boeking
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening
import com.vdzon.administratie.model.*
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDFont
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject

import java.io.*
import java.net.URL
import java.time.LocalDate
import java.util.stream.Collectors

class GenerateOverzicht {
    private var pos = 0f
    private var pageHeight = 0f
    private var pageWidth = 0f
    private var page: PDPageContentStream? = null
    private var document: PDDocument? = null
    private var pdfPage: PDPage? = null

    @Throws(IOException::class)
    private fun start(administratie: Administratie, beginDate: String, endDate: String, outputStream: BufferedOutputStream) {
        document = PDDocument()
        pdfPage = PDPage(PDRectangle.A4)
        val rect = pdfPage!!.mediaBox
        document!!.addPage(pdfPage)
        page = PDPageContentStream(document!!, pdfPage!!)

        pageHeight = rect.height
        pageWidth = rect.width
        pos = pageHeight

        val boekingenCache = BoekingenCache(administratie.boekingen)


        skipDown(25f)
        val administratieGegevens = administratie.administratieGegevens
        if (administratieGegevens != null) {
            addAnImage(document!!, administratieGegevens.logoUrl)
        }
        skipDown(20f)
        writeBoldText("Overzichten voor periode $beginDate tot en met $endDate")
        skipDown(20f)

        val overzicht = CalculateOverzicht.calculateOverzicht(administratie, beginDate, endDate)

        writeTitle("Inkomsten")
        writeRegelsBetaaldOntvangenRegel(fontBold, "", "Prijs excl.", "Btw", "Prijs incl.")
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Uitgeschreven facturen", overzicht.facturenTotaalExBtw, overzicht.facturenTotaalBtw, overzicht.facturenTotaalIncBtw)
        skipDown(30f)
        writeTitle("Uitgaven")
        writeRegelsBetaaldOntvangenRegel(fontBold, "", "Prijs excl.", "Btw", "Prijs incl.")
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Betaalde rekeningen", overzicht.rekeningenTotaalExBtw, overzicht.rekeningenTotaalBtw, overzicht.rekeningenTotaalIncBtw)
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Declaraties", overzicht.declaratiesTotaalExBtw, overzicht.declaratiesTotaalBtw, overzicht.declaratiesTotaalIncBtw)
        skipDown(30f)
        writeTitle("Totaal belastbaar inkomen")
        writeFieldValue(fontPlain, "Totaal Exclusief BTW", overzicht.belastbaarInkomenExBtw)
        writeFieldValue(fontPlain, "BTW", overzicht.belastbaarInkomenBtw)
        writeFieldValue(fontPlain, "Totaal Inclusief BTW", overzicht.belastbaarInkomenIncBtw)
        skipDown(30f)
        writeTitle("Bankrekening controle")


        writeFieldValue(fontPlain, "Betaald gekregen facturen van deze periode, ontvangen binnen deze periode", overzicht.ontvangenFactuurBetalingenBetaaldBinnenGeselecteerdePeriode)
        writeFieldValue(fontPlain, "Betaald gekregen facturen van deze periode, ontvangen buiten deze periode", overzicht.ontvangenFacturenBetaaldBuitenGeselecteerdePeriode)
        writeFieldValue(fontPlain, "Betaald gekregen facturen buiten deze periode, ontvangen binnen deze periode", overzicht.ontvangenFactuurBetalingenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode)
        writeFieldValue(fontPlain, "Onbetaalde facturen", overzicht.onbetaaldeFacturen)

        skipDown(10f)

        writeFieldValue(fontPlain, "Betaalde rekeningen van deze periode, betaald binnen deze periode", overzicht.betaaldeRekeningenBetaaldBinnenGeselecteerdePeriode)
        writeFieldValue(fontPlain, "Betaalde rekeningen van deze periode, betaald buiten deze periode", overzicht.betaaldeRekeningenBetaaldBuitenGeselecteerdePeriode)
        writeFieldValue(fontPlain, "Betaalde rekeningen buiten deze periode, betaald binnen deze periode", overzicht.betaaldeRekeningenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode)
        writeFieldValue(fontPlain, "Onbetaalde rekeningen", overzicht.onbetaaldeRekeningen)

        skipDown(10f)

        writeFieldValue(fontPlain, "Prive boekingen gedaan", overzicht.priveBoekingen)
        writeFieldValue(fontPlain, "Ontvangen betalingen op bank waarvan geen factuur van is", overzicht.ontvangenInkomstenZonderFactuur)
        writeFieldValue(fontPlain, "Betaalde rekeningen waar geen factuur van is", overzicht.betaaldeRekeningenZonderFactuur)


        writeFieldValue(fontPlain, "Verwacht totaal op rekening bij", overzicht.verwachtTotaalOpRekeningBij)
        writeFieldValue(fontPlain, "Werkelijk op bank bijgeschreven", overzicht.werkelijkOpBankBij)
        skipDown(10f)
        writeFieldValue(fontPlain, "Verschil tussen verwacht en werkelijk ontvangen", overzicht.verschilTussenVerwachtEnWerkelijk)

        page!!.close()

        /*
********************** FACTUREN PAGINA
 */
        pos = pageHeight


        pdfPage = PDPage(PDRectangle.A4)
        document!!.addPage(pdfPage)
        page = PDPageContentStream(document!!, pdfPage!!)

        skipDown(10f)
        writeTitle("Alle uitgeschreven facturen")
        //        skipDown(10);
        //        listFactuurHeader();
        overzicht
                .filteredFacturen!!
                .sortedBy {  it.factuurNummer}
                .reversed()
                .forEach { factuur -> listFactuur(factuur, boekingenCache) }

        page!!.close()

        /*
********************** REKENINGEN PAGINA
 */
        pos = pageHeight

        pdfPage = PDPage(PDRectangle.A4)
        document!!.addPage(pdfPage)
        page = PDPageContentStream(document!!, pdfPage!!)

        skipDown(10f)
        writeTitle("Alle rekeningen")
        overzicht
                .filteredRekeningen!!
                .sortedBy{it.rekeningNummer}
                .reversed()
                .forEach { rekening -> listRekening(rekening, boekingenCache) }

        page!!.close()

        /*
********************** DECLARATIES PAGINA
 */
        pos = pageHeight

        pdfPage = PDPage(PDRectangle.A4)
        document!!.addPage(pdfPage)
        page = PDPageContentStream(document!!, pdfPage!!)

        skipDown(10f)
        writeTitle("Alle declaraties")
        overzicht.filteredDeclaraties!!
                .sortedBy { it.declaratieNummer }
                .reversed()
                .forEach { declaratie -> listDeclaraties(declaratie) }

        page!!.close()
        /*
********************** AFSCHRIFTEN PAGINA
 */
        pos = pageHeight

        pdfPage = PDPage(PDRectangle.A4)
        document!!.addPage(pdfPage)
        page = PDPageContentStream(document!!, pdfPage!!)

        skipDown(10f)
        writeTitle("Alle bank afschrijvingen")
        skipDown(10f)
        overzicht
                .filteredAfschriften!!
                .sortedBy{it.nummer}
                .reversed()
                .forEach { afschrift -> listAfschrift(afschrift, boekingenCache) }

        page!!.close()

        /*
********************** OUDE REKENINGEN MET LOPENDE AFSCHRIJVINGEN
 */
        pos = pageHeight

        pdfPage = PDPage(PDRectangle.A4)
        document!!.addPage(pdfPage)
        page = PDPageContentStream(document!!, pdfPage!!)

        skipDown(10f)
        writeTitle("Alle rekeningen met nog lopende afschrijvingen")
        administratie
                .rekeningen
                .filter{ rekening -> rekeningHeeftLopendeAfschrijving(rekening, overzicht.beginDate) }
                .sortedBy{it.rekeningNummer}
                .reversed()
                .forEach { rekening -> listRekening(rekening, boekingenCache) }

        page!!.close()
        /*
********************** WAARSCHUWINGEN PAGINA
 */
        pos = pageHeight

        pdfPage = PDPage(PDRectangle.A4)
        document!!.addPage(pdfPage)
        page = PDPageContentStream(document!!, pdfPage!!)

        skipDown(10f)
        val checkAndFixRegels = CheckService.getCheckAndFixRegels(administratie).
                filter{ regel -> betweenOrAtDates(regel.date, overzicht.beginDate!!, overzicht.endDate!!) }
        if (checkAndFixRegels.isEmpty()) {
            writeTitle("Geen waarschuwingen gevonden")
        } else {
            writeTitle("Alle waarschuwingen")
            checkAndFixRegels.forEach { regel -> listWaarschuwing(regel) }
        }

        page!!.close()
        /*
********************** CLOSE
 */


        document!!.save(outputStream)
        document!!.close()
    }

    private fun rekeningHeeftLopendeAfschrijving(rekening: Rekening, beginDate: LocalDate?): Boolean {
        if (rekening.maandenAfschrijving == 0) return false
        return rekening.rekeningDate.plusMonths(rekening.maandenAfschrijving.toLong()).isAfter(beginDate)
    }

    private fun listWaarschuwing(regel: CheckAndFixRegel2) {
        try {
            skipDown(15f)
            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontPlain, regel.omschrijving)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun listFactuur(factuur: Factuur, boekingenCache: BoekingenCache) {
        val boekingenVanFactuur = boekingenCache.getBoekingenVanFactuur(factuur.factuurNummer)
        var status = "Geen betaling gevonden in de afschriften"
        if (boekingenVanFactuur != null && boekingenVanFactuur.size > 0) {
            status = "Betaald"
        }

        try {
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Nummer:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, factuur.factuurNummer)
            skipDown(15f)

            val contact = factuur.contact
            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Klant:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, contact!!.naam)
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Datum:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, factuur.factuurDate.toString())
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Bedrag ex Btw:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", factuur.bedragExBtw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "BTW:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", factuur.btw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Bedrag inc Btw:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", factuur.bedragIncBtw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Status:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, status)
            skipDown(15f)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }


    private fun listAfschrift(afschrift: Afschrift, boekingenCache: BoekingenCache) {
        try {
            val boekingenVanAfschrift = boekingenCache.getBoekingenVanAfschrift(afschrift.nummer)
            var status = ""
            for (boekingMetAfschrift in boekingenVanAfschrift) {
                status += (boekingMetAfschrift as Boeking).omschrijving + " "
                if (boekingMetAfschrift is BoekingMetFactuur) {
                    status += "(factuur " + boekingMetAfschrift.factuurNummer + ") "
                }
                if (boekingMetAfschrift is BoekingMetRekening) {
                    status += "(rekening " + boekingMetAfschrift.rekeningNummer + ") "
                }
            }

            skipDown(15f)


            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Nummer:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, afschrift.nummer)
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Datum:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, afschrift.boekdatum.toString())
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Bedrag:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, "" + afschrift.bedrag)
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Geboekt als:")
            val statusInPartsOf80Chars = status.split("(?<=\\G.{80})".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            var first = true
            for (statusPartOf80Chars in statusInPartsOf80Chars) {
                if (!first) {
                    skipDown(15f)
                }
                first = false
                writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, statusPartOf80Chars)
            }


            skipDown(15f)
            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Naam:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, afschrift.relatienaam)
            skipDown(15f)
            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Omschrijving:")

            val omschrijvingInPartsOf80Chars = afschrift.omschrijving.split("(?<=\\G.{80})".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            first = true
            for (omschrijvingPartOf80Chars in omschrijvingInPartsOf80Chars) {
                if (!first) {
                    skipDown(15f)
                }
                first = false
                writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, omschrijvingPartOf80Chars)
            }
            skipDown(8f)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun listRekeningHeader() {
        try {
            skipDown(15f)
            var y = 30
            writeText(LIJST_FONT_SIZE.toFloat(), y.toFloat(), fontBold, "Nummer")
            y += 50
            writeText(LIJST_FONT_SIZE.toFloat(), y.toFloat(), fontBold, "Datum")
            y += 60
            writeText(LIJST_FONT_SIZE.toFloat(), y.toFloat(), fontBold, "Bedrag ex Btw")
            y += 80
            writeText(LIJST_FONT_SIZE.toFloat(), y.toFloat(), fontBold, "BTW")
            y += 40
            writeText(LIJST_FONT_SIZE.toFloat(), y.toFloat(), fontBold, "Bedrag inc Btw")
            y += 80
            writeText(LIJST_FONT_SIZE.toFloat(), y.toFloat(), fontBold, "Status")
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun listRekening(rekening: Rekening, boekingenCache: BoekingenCache) {
        val boekingenVanRekening = boekingenCache.getBoekingenVanRekening(rekening.rekeningNummer)
        var status = "Geen betaling gevonden in de afschriften"
        if (boekingenVanRekening != null && boekingenVanRekening.size > 0) {
            status = "Betaald"
        }
        if (rekening.maandenAfschrijving > 0) {
            status += " (afschrijven in " + rekening.maandenAfschrijving + " maanden)"

        }

        try {
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Nummer:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, rekening.rekeningNummer)
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Factuurnummer:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, rekening.factuurNummer)
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Datum:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, rekening.rekeningDate.toString())
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Bedrijf:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, rekening.naam.toString())
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Omschrijving:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, rekening.omschrijving.toString())
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Bedrag ex Btw")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", rekening.bedragExBtw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "BTW:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", rekening.btw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Bedrag inc Btw:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", rekening.bedragIncBtw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Status:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, status)
            skipDown(15f)

        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun listDeclaraties(declaratie: Declaratie) {
        try {
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Nummer:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, declaratie.declaratieNummer)
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Datum:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, declaratie.declaratieDate.toString())
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Bedrag ex Btw:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", declaratie.bedragExBtw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "BTW:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", declaratie.btw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Bedrag inc Btw:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, String.format("%.2f", declaratie.bedragIncBtw))
            skipDown(15f)

            writeText(LIJST_FONT_SIZE.toFloat(), 30f, fontBold, "Omschrijving:")
            writeText(LIJST_FONT_SIZE.toFloat(), 120f, fontPlain, declaratie.omschrijving.toString())
            skipDown(15f)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    private fun addAnImage(document: PDDocument, logoUrl: String) {
        try {
            val url = URL(logoUrl)
            val `in` = BufferedInputStream(url.openStream())
            val out = ByteArrayOutputStream()
            val buf = ByteArray(1024)
            var n = 0
            while (-1 != {n = `in`.read(buf);n}()) {
                out.write(buf, 0, n)
            }
            out.close()
            `in`.close()
            val response = out.toByteArray()
            val fos = FileOutputStream("logo.png")
            fos.write(response)
            fos.close()

            val ximage = PDImageXObject.createFromFile("logo.png", document)
            val scale = 0.5f // alter this value to set the image size
            skipDown(ximage.height * scale)
            page!!.drawImage(ximage, 30f, pos, ximage.width * scale, ximage.height * scale)
        } catch (ioex: IOException) {
            println("No image for you")
        }

    }

    //----------------------------------------------------

    @Throws(IOException::class)
    private fun writeRegelsBetaaldOntvangenRegel(font: PDFont, titel1: String, titel2: Double, titel3: Double, titel4: Double) {
        writeRegelsBetaaldOntvangenRegel(font, titel1, String.format("%.2f", titel2), String.format("%.2f", titel3), String.format("%.2f", titel4))
    }

    @Throws(IOException::class)
    private fun writeRegelsBetaaldOntvangenRegel(font: PDFont, titel1: String, titel2: String, titel3: String, titel4: String) {
        skipDown(15f)
        writeText(12f, 30f, font, titel1)
        writeText(12f, 180f, font, titel2)
        writeText(12f, 280f, font, titel3)
        writeText(12f, 380f, font, titel4)
    }

    @Throws(IOException::class)
    private fun writeNormalText(text: String) {
        skipDown(15f)
        writeText(12f, fontPlain, text)
    }

    @Throws(IOException::class)
    private fun writeBoldText(text: String) {
        skipDown(15f)
        writeText(12f, fontBold, text)
    }

    @Throws(IOException::class)
    private fun writeTitle(text: String) {
        skipDown(20f)
        writeText(19f, fontBold, text)
    }

    @Throws(IOException::class)
    private fun writeText(fontSize: Float, fontPlain: PDFont, text: String) {
        writeText(fontSize, 30f, fontPlain, text)
    }


    @Throws(IOException::class)
    private fun writeText(fontSize: Float, x: Float, fontPlain: PDFont, text: String?) {
        if (text == null) return
        page!!.beginText()
        page!!.setFont(fontPlain, fontSize)
        page!!.newLineAtOffset(x, pos)
        page!!.showText(text)
        page!!.endText()
    }


    @Throws(IOException::class)
    private fun writeFieldValue(font: PDFont, titel1: String, titel3: Double) {
        writeFieldValue(font, titel1, String.format("%.2f", titel3))
    }

    @Throws(IOException::class)
    private fun writeFieldValue(font: PDFont, titel1: String, titel3: String) {
        skipDown(15f)
        writeText(12f, 30f, font, titel1)
        writeText(12f, 500f, font, ":")
        writeText(12f, 505f, font, titel3)
    }

    @Throws(IOException::class)
    private fun skipDown(amount: Float): Float {
        pos -= amount
        if (pos < 30) {
            page!!.close()
            pos = pageHeight - 30
            pdfPage = PDPage(PDRectangle.A4)
            document!!.addPage(pdfPage)
            page = PDPageContentStream(document!!, pdfPage!!)
        }
        return pos

    }


    @Throws(IOException::class)
    private fun drawLine() {
        skipDown(5f)
        page!!.setLineWidth(1f)
        page!!.moveTo(0f, pos)
        page!!.lineTo(pageWidth, pos)
        page!!.closeAndStroke()
    }

    companion object {

        //Todo: deze class opruimen

        private val fontPlain = PDType1Font.HELVETICA
        private val fontBold = PDType1Font.HELVETICA_BOLD

        @Throws(IOException::class)
        fun buildPdf(administratie: Administratie, beginDate: String, endDate: String, outputStream: BufferedOutputStream) {
            GenerateOverzicht().start(administratie, beginDate, endDate, outputStream)
        }

        private fun betweenOrAtDates(date: LocalDate?, beginDate: LocalDate, endData: LocalDate): Boolean {
            if (date == null) return true
            return date == beginDate || date == endData || betweenDates(date, beginDate, endData)
        }

        private fun betweenDates(date: LocalDate, beginDate: LocalDate, endData: LocalDate): Boolean {
            return date.isAfter(beginDate) && date.isBefore(endData)
        }

        private val LIJST_FONT_SIZE = 10
    }

}
