package com.vdzon.administratie.pdfgenerator.overzicht;

import com.vdzon.administratie.checkandfix.CheckServiceScala;
import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;

import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import com.vdzon.administratie.model.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateOverzicht {

//Todo: deze class opruimen

    private static PDFont fontPlain = PDType1Font.HELVETICA;
    private static PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    private float pos = 0;
    private float pageHeight = 0;
    private float pageWidth = 0;
    private PDPageContentStream page;
    private PDDocument document = null;
    private PDPage pdfPage = null;

    public static void buildPdf(Administratie administratie, String beginDate, String endDate, BufferedOutputStream outputStream) throws IOException {
        new GenerateOverzicht().start(administratie, beginDate, endDate, outputStream);
    }

    private void start(Administratie administratie, String beginDate, String endDate, BufferedOutputStream outputStream) throws IOException {
        document = new PDDocument();
        pdfPage = new PDPage(PDRectangle.A4);
        PDRectangle rect = pdfPage.getMediaBox();
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        pageHeight = rect.getHeight();
        pageWidth = rect.getWidth();
        pos = pageHeight;

        BoekingenCache boekingenCache = new BoekingenCache(administratie.getBoekingen());


        skipDown(25);
        AdministratieGegevens administratieGegevens = administratie.getAdministratieGegevens();
        if (administratieGegevens != null) {
            addAnImage(document, administratieGegevens .getLogoUrl());
        }
        skipDown(20);
        writeBoldText("Overzichten voor periode "+beginDate+" tot en met "+endDate);
        skipDown(20);

        Overzicht overzicht = CalculateOverzicht.calculateOverzicht(administratie, beginDate, endDate);

        writeTitle("Inkomsten");
        writeRegelsBetaaldOntvangenRegel(fontBold, "", "Prijs excl.", "Btw", "Prijs incl.");
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Uitgeschreven facturen", overzicht.facturenTotaalExBtw, overzicht.facturenTotaalBtw, overzicht.facturenTotaalIncBtw);
        skipDown(30);
        writeTitle("Uitgaven");
        writeRegelsBetaaldOntvangenRegel(fontBold, "", "Prijs excl.", "Btw", "Prijs incl.");
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Betaalde rekeningen", overzicht.rekeningenTotaalExBtw, overzicht.rekeningenTotaalBtw, overzicht.rekeningenTotaalIncBtw);
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Declaraties", overzicht.declaratiesTotaalExBtw, overzicht.declaratiesTotaalBtw, overzicht.declaratiesTotaalIncBtw);
        skipDown(30);
        writeTitle("Totaal belastbaar inkomen");
        writeFieldValue(fontPlain, "Totaal Exclusief BTW", overzicht.belastbaarInkomenExBtw);
        writeFieldValue(fontPlain, "BTW", overzicht.belastbaarInkomenBtw);
        writeFieldValue(fontPlain, "Totaal Inclusief BTW", overzicht.belastbaarInkomenIncBtw);
        skipDown(30);
        writeTitle("Bankrekening controle");


        writeFieldValue(fontPlain, "Betaald gekregen facturen van deze periode, ontvangen binnen deze periode", overzicht.ontvangenFactuurBetalingenBetaaldBinnenGeselecteerdePeriode);
        writeFieldValue(fontPlain, "Betaald gekregen facturen van deze periode, ontvangen buiten deze periode", overzicht.ontvangenFacturenBetaaldBuitenGeselecteerdePeriode);
        writeFieldValue(fontPlain, "Betaald gekregen facturen buiten deze periode, ontvangen binnen deze periode", overzicht.ontvangenFactuurBetalingenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode);
        writeFieldValue(fontPlain, "Onbetaalde facturen", overzicht.onbetaaldeFacturen);

        skipDown(10);

        writeFieldValue(fontPlain, "Betaalde rekeningen van deze periode, betaald binnen deze periode", overzicht.betaaldeRekeningenBetaaldBinnenGeselecteerdePeriode);
        writeFieldValue(fontPlain, "Betaalde rekeningen van deze periode, betaald buiten deze periode", overzicht.betaaldeRekeningenBetaaldBuitenGeselecteerdePeriode);
        writeFieldValue(fontPlain, "Betaalde rekeningen buiten deze periode, betaald binnen deze periode", overzicht.betaaldeRekeningenVanBuitenGeselecteerdePeriodeBetaaldBinnenGeselecteerdePeriode);
        writeFieldValue(fontPlain, "Onbetaalde rekeningen", overzicht.onbetaaldeRekeningen);

        skipDown(10);

        writeFieldValue(fontPlain, "Prive boekingen gedaan", overzicht.priveBoekingen);
        writeFieldValue(fontPlain, "Ontvangen betalingen op bank waarvan geen factuur van is", overzicht.ontvangenInkomstenZonderFactuur);
        writeFieldValue(fontPlain, "Betaalde rekeningen waar geen factuur van is", overzicht.betaaldeRekeningenZonderFactuur);


        writeFieldValue(fontPlain, "Verwacht totaal op rekening bij", overzicht.verwachtTotaalOpRekeningBij);
        writeFieldValue(fontPlain, "Werkelijk op bank bijgeschreven", overzicht.werkelijkOpBankBij);
        skipDown(10);
        writeFieldValue(fontPlain, "Verschil tussen verwacht en werkelijk ontvangen", overzicht.verschilTussenVerwachtEnWerkelijk);

        page.close();

/*
********************** FACTUREN PAGINA
 */
        pos = pageHeight;


        pdfPage = new PDPage(PDRectangle.A4);
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        skipDown(10);
        writeTitle("Alle uitgeschreven facturen");
//        skipDown(10);
//        listFactuurHeader();
        overzicht
                .filteredFacturen
                .stream()
                .sorted((fak1, fak2) -> fak2.getFactuurNummer().compareTo(fak1.getFactuurNummer()))
                .forEach(factuur -> listFactuur(factuur, boekingenCache));

        page.close();

/*
********************** REKENINGEN PAGINA
 */
        pos = pageHeight;

        pdfPage = new PDPage(PDRectangle.A4);
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        skipDown(10);
        writeTitle("Alle rekeningen");
        overzicht
                .filteredRekeningen
                .stream()
                .sorted((rek1, rek2) -> rek2.getRekeningNummer().compareTo(rek1.getRekeningNummer()))
                .forEach(rekening -> listRekening(rekening, boekingenCache));

        page.close();

/*
********************** DECLARATIES PAGINA
 */
        pos = pageHeight;

        pdfPage = new PDPage(PDRectangle.A4);
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        skipDown(10);
        writeTitle("Alle declaraties");
        overzicht.filteredDeclaraties
                .stream()
                .sorted((decl1, decl2) -> decl2.getDeclaratieNummer().compareTo(decl1.getDeclaratieNummer()))
                .forEach(declaratie-> listDeclaraties(declaratie));

        page.close();
/*
********************** AFSCHRIFTEN PAGINA
 */
        pos = pageHeight;

        pdfPage = new PDPage(PDRectangle.A4);
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        skipDown(10);
        writeTitle("Alle bank afschrijvingen");
        skipDown(10);
        overzicht
                .filteredAfschriften
                .stream()
                .sorted((afschrift1, afschrift2) -> afschrift2.getNummer().compareTo(afschrift1.getNummer()))
                .forEach(afschrift-> listAfschrift(afschrift, boekingenCache));

        page.close();

/*
********************** OUDE REKENINGEN MET LOPENDE AFSCHRIJVINGEN
 */
        pos = pageHeight;

        pdfPage = new PDPage(PDRectangle.A4);
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        skipDown(10);
        writeTitle("Alle rekeningen met nog lopende afschrijvingen");
        administratie
                .getRekeningen()
                .stream()
                .filter(rekening -> rekeningHeeftLopendeAfschrijving(rekening, overzicht.beginDate))
                .sorted((rek1, rek2) -> rek2.getRekeningNummer().compareTo(rek1.getRekeningNummer()))
                .forEach(rekening -> listRekening(rekening, boekingenCache));

        page.close();
/*
********************** WAARSCHUWINGEN PAGINA
 */
        pos = pageHeight;

        pdfPage = new PDPage(PDRectangle.A4);
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        skipDown(10);
        List<CheckAndFixRegel> checkAndFixRegels = CheckServiceScala.getCheckAndFixRegels(administratie).stream().filter(regel->betweenOrAtDates(regel.date(),overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());
        if (checkAndFixRegels.isEmpty()){
            writeTitle("Geen waarschuwingen gevonden");
        }
        else{
            writeTitle("Alle waarschuwingen");
            checkAndFixRegels.stream().forEach(regel->listWaarschuwing(regel));
        }

        page.close();
/*
********************** CLOSE
 */


        document.save(outputStream);
        document.close();
    }

    private boolean rekeningHeeftLopendeAfschrijving(Rekening rekening, LocalDate beginDate) {
        if (rekening.getMaandenAfschrijving()==0) return false;
        return rekening.getRekeningDate().plusMonths(rekening.getMaandenAfschrijving()).isAfter(beginDate);
    }

    private static boolean betweenOrAtDates(LocalDate date, LocalDate beginDate, LocalDate endData){
        if (date==null) return true;
        return date.equals(beginDate) || date.equals(endData) || betweenDates(date, beginDate, endData);
    }

    private static boolean betweenDates(LocalDate date, LocalDate beginDate, LocalDate endData){
        return date.isAfter(beginDate) && date.isBefore(endData);
    }

    private void listWaarschuwing(CheckAndFixRegel regel) {
        try {
            skipDown(15);
            writeText(LIJST_FONT_SIZE, 30, fontPlain, regel.omschrijving());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static int LIJST_FONT_SIZE = 10;

    private void listFactuur(Factuur factuur, BoekingenCache boekingenCache)  {
        List<BoekingMetFactuur> boekingenVanFactuur = boekingenCache.getBoekingenVanFactuur(factuur.getFactuurNummer());
        String status = "Geen betaling gevonden in de afschriften";
        if (boekingenVanFactuur!=null && boekingenVanFactuur.size()>0){
            status = "Betaald";
        }

        try {
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Nummer:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, factuur.getFactuurNummer());
            skipDown(15);

            Contact contact = factuur.getContact();
            writeText(LIJST_FONT_SIZE, 30, fontBold, "Klant:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, contact.getNaam());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Datum:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, factuur.getFactuurDate().toString());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Bedrag ex Btw:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",factuur.getBedragExBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "BTW:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",factuur.getBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Bedrag inc Btw:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",factuur.getBedragIncBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Status:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, status);
            skipDown(15);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }


    private void listAfschrift(Afschrift afschrift, BoekingenCache boekingenCache)  {
        try {
            List<BoekingMetAfschrift> boekingenVanAfschrift = boekingenCache.getBoekingenVanAfschrift(afschrift.getNummer());
            String status = "";
            for (BoekingMetAfschrift boekingMetAfschrift : boekingenVanAfschrift){
                status += ((Boeking)boekingMetAfschrift).getOmschrijving()+" ";
                if (boekingMetAfschrift instanceof BoekingMetFactuur){
                    status += "(factuur "+((BoekingMetFactuur) boekingMetAfschrift).getFactuurNummer()+") ";
                }
                if (boekingMetAfschrift instanceof BoekingMetRekening){
                    status += "(rekening "+((BoekingMetRekening) boekingMetAfschrift).getRekeningNummer()+") ";
                }
            }

            skipDown(15);


            writeText(LIJST_FONT_SIZE, 30, fontBold, "Nummer:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, afschrift.getNummer());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Datum:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, afschrift.getBoekdatum().toString());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Bedrag:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, ""+afschrift.getBedrag());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Geboekt als:");
            String[] statusInPartsOf80Chars = status.split("(?<=\\G.{80})");
            boolean first = true;
            for (String statusPartOf80Chars: statusInPartsOf80Chars){
                if (!first){
                    skipDown(15);
                }
                first = false;
                writeText(LIJST_FONT_SIZE, 120, fontPlain, statusPartOf80Chars);
            }


            skipDown(15);
            writeText(LIJST_FONT_SIZE, 30, fontBold, "Naam:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, afschrift.getRelatienaam());
            skipDown(15);
            writeText(LIJST_FONT_SIZE, 30, fontBold, "Omschrijving:");

            String[] omschrijvingInPartsOf80Chars = afschrift.getOmschrijving().split("(?<=\\G.{80})");
            first = true;
            for (String omschrijvingPartOf80Chars: omschrijvingInPartsOf80Chars){
                if (!first){
                    skipDown(15);
                }
                first = false;
                writeText(LIJST_FONT_SIZE, 120, fontPlain, omschrijvingPartOf80Chars);
            }
            skipDown(8);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void listRekeningHeader()  {
        try {
            skipDown(15);
            int y = 30;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Nummer");
            y+=50;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Datum");
            y+=60;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Bedrag ex Btw");
            y+=80;
            writeText(LIJST_FONT_SIZE, y, fontBold, "BTW");
            y+=40;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Bedrag inc Btw");
            y+=80;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Status");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void listRekening(Rekening rekening, BoekingenCache boekingenCache)  {
        List<BoekingMetRekening> boekingenVanRekening = boekingenCache.getBoekingenVanRekening(rekening.getRekeningNummer());
        String status = "Geen betaling gevonden in de afschriften";
        if (boekingenVanRekening!=null && boekingenVanRekening.size()>0){
            status = "Betaald";
        }
        if (rekening.getMaandenAfschrijving()>0){
            status += " (afschrijven in "+rekening.getMaandenAfschrijving()+" maanden)";

        }

        try {
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Nummer:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, rekening.getRekeningNummer());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Datum:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, rekening.getRekeningDate().toString());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Bedrijf:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, rekening.getNaam().toString());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Omschrijving:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, rekening.getOmschrijving().toString());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Bedrag ex Btw");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",rekening.getBedragExBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "BTW:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",rekening.getBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Bedrag inc Btw:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",rekening.getBedragIncBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Status:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, status);
            skipDown(15);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void listDeclaraties(Declaratie declaratie)  {
        try {
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Nummer:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, declaratie.getDeclaratieNummer());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Datum:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, declaratie.getDeclaratieDate().toString());
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Bedrag ex Btw:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",declaratie.getBedragExBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "BTW:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",declaratie.getBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Bedrag inc Btw:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, String.format("%.2f",declaratie.getBedragIncBtw()));
            skipDown(15);

            writeText(LIJST_FONT_SIZE, 30, fontBold, "Omschrijving:");
            writeText(LIJST_FONT_SIZE, 120, fontPlain, declaratie.getOmschrijving().toString());
            skipDown(15);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void addAnImage(PDDocument document, String logoUrl) {
        try {
            URL url = new URL(logoUrl);
            InputStream in = new BufferedInputStream(url.openStream());
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int n = 0;
            while (-1!=(n=in.read(buf)))
            {
                out.write(buf, 0, n);
            }
            out.close();
            in.close();
            byte[] response = out.toByteArray();
            FileOutputStream fos = new FileOutputStream("logo.png");
            fos.write(response);
            fos.close();

            PDImageXObject ximage = PDImageXObject.createFromFile("logo.png", document);
            float scale = 0.5f; // alter this value to set the image size
            skipDown(ximage.getHeight() * scale);
            page.drawImage(ximage, 30, pos, ximage.getWidth() * scale, ximage.getHeight() * scale);
        } catch (IOException ioex) {
            System.out.println("No image for you");
        }
    }

    //----------------------------------------------------

    private void writeRegelsBetaaldOntvangenRegel(PDFont font, String titel1, double titel2, double titel3, double titel4) throws IOException {
        writeRegelsBetaaldOntvangenRegel(font, titel1, String.format("%.2f", titel2), String.format("%.2f", titel3), String.format("%.2f", titel4));
    }

    private void writeRegelsBetaaldOntvangenRegel(PDFont font, String titel1, String titel2, String titel3, String titel4) throws IOException {
        skipDown(15);
        writeText(12, 30, font, titel1);
        writeText(12, 180, font, titel2);
        writeText(12, 280, font, titel3);
        writeText(12, 380, font, titel4);
    }

    private void writeNormalText(String text) throws IOException {
        skipDown(15);
        writeText(12, fontPlain, text);
    }

    private void writeBoldText(String text) throws IOException {
        skipDown(15);
        writeText(12, fontBold, text);
    }

    private void writeTitle(String text) throws IOException {
        skipDown(20);
        writeText(19, fontBold, text);
    }

    private void writeText(float fontSize, PDFont fontPlain, String text) throws IOException {
        writeText(fontSize, 30, fontPlain, text);
    }


    private void writeText(float fontSize, float x, PDFont fontPlain, String text) throws IOException {
        if (text == null) return;
        page.beginText();
        page.setFont(fontPlain, fontSize);
        page.newLineAtOffset(x, pos);
        page.showText(text);
        page.endText();
    }



    private void writeFieldValue(PDFont font, String titel1, double titel3) throws IOException {
        writeFieldValue(font, titel1, String.format("%.2f", titel3));
    }

    private void writeFieldValue(PDFont font, String titel1, String titel3) throws IOException {
        skipDown(15);
        writeText(12, 30, font, titel1);
        writeText(12, 500, font, ":");
        writeText(12, 505, font, titel3);
    }

    private float skipDown(float amount) throws IOException {
        pos -= amount;
        if (pos<30) {
            page.close();
            pos = pageHeight-30;
            pdfPage = new PDPage(PDRectangle.A4);
            document.addPage(pdfPage);
            page = new PDPageContentStream(document, pdfPage);
        }
        return pos;

    }


    private void drawLine() throws IOException {
        skipDown(5);
        page.setLineWidth(1);
        page.moveTo(0, pos);
        page.lineTo(pageWidth, pos);
        page.closeAndStroke();
    }

}
