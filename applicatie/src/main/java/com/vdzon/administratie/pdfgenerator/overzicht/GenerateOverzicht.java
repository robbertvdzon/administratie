package com.vdzon.administratie.pdfgenerator.overzicht;

import com.vdzon.administratie.checkandfix.model.CheckAndFixRegel;
import com.vdzon.administratie.rest.checkandfix.CheckAndFixService;
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

    public static void buildPdf(Administratie administratie, String beginDate, String endDate, BufferedOutputStream outputStream, CheckAndFixService checkAndFixService) throws IOException {
        new GenerateOverzicht().start(administratie, beginDate, endDate, outputStream, checkAndFixService);
    }

    private void start(Administratie administratie, String beginDate, String endDate, BufferedOutputStream outputStream, CheckAndFixService checkAndFixService) throws IOException {
        document = new PDDocument();
        pdfPage = new PDPage(PDRectangle.A4);
        PDRectangle rect = pdfPage.getMediaBox();
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        pageHeight = rect.getHeight();
        pageWidth = rect.getWidth();
        pos = pageHeight;


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
        writeFieldValue(fontPlain, "Betaalde facturen", overzicht.betaaldeFacturen);
        writeFieldValue(fontPlain, "Onbetaalde facturen", overzicht.onbetaaldeFacturen);
        writeFieldValue(fontPlain, "Betaalde rekeningen", overzicht.betaaldeRekeningen);
        writeFieldValue(fontPlain, "Betaalde facturen buiten geselecteerde periode", overzicht.betaaldeFacturenBuitenGeselecteerdePeriode);
        writeFieldValue(fontPlain, "Betaalde rekeningen buiten geselecteerde periode", overzicht.betaaldeRekeningenBuitenGeselecteerdePeriode);
        writeFieldValue(fontPlain, "Verwacht totaal op rekening bij", overzicht.verwachtTotaalOpRekeningBij);
        skipDown(10);
        writeFieldValue(fontPlain, "Werkelijk op bank bijgeschreven", overzicht.werkelijkOpBankBij);
        writeFieldValue(fontPlain, "Prive boekingen op bank", overzicht.priveOpBankBij);
        writeFieldValue(fontPlain, "Werkelijk totaal voor administratie", overzicht.werkelijkOpBankBijVoorAdministratie);
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
        writeTitle("Alle facturen");
        skipDown(10);
        listFactuurHeader();
        overzicht
                .filteredFacturen
                .stream()
                .sorted((fak1, fak2) -> fak2.getFactuurNummer().compareTo(fak1.getFactuurNummer()))
                .forEach(factuur -> listFactuur(factuur));

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
        skipDown(10);
        listRekeningHeader();
        overzicht
                .filteredRekeningen
                .stream()
                .sorted((rek1, rek2) -> rek2.getRekeningNummer().compareTo(rek1.getRekeningNummer()))
                .forEach(rekening -> listRekening(rekening));

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
        skipDown(10);
        listDeclaratiesHeader();
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
        writeTitle("Alle afschriften");
        skipDown(10);
        listAfschriftHeader();
        overzicht
                .filteredAfschriften
                .stream()
                .sorted((afschrift1, afschrift2) -> afschrift2.getNummer().compareTo(afschrift1.getNummer()))
                .forEach(afschrift-> listAfschrift(afschrift));

        page.close();

/*
********************** WAARSCHUWINGEN PAGINA
 */
        pos = pageHeight;

        pdfPage = new PDPage(PDRectangle.A4);
        document.addPage(pdfPage);
        page = new PDPageContentStream(document, pdfPage);

        skipDown(10);
        List<CheckAndFixRegel> checkAndFixRegels = checkAndFixService.getCheckAndFixRegels(administratie).stream().filter(regel->betweenOrAtDates(regel.getDate(),overzicht.beginDate, overzicht.endDate)).collect(Collectors.toList());
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

    private static boolean betweenOrAtDates(LocalDate date, LocalDate beginDate, LocalDate endData){
        return date.equals(beginDate) || date.equals(endData) || betweenDates(date, beginDate, endData);
    }

    private static boolean betweenDates(LocalDate date, LocalDate beginDate, LocalDate endData){
        return date.isAfter(beginDate) && date.isBefore(endData);
    }

    private void listWaarschuwing(CheckAndFixRegel regel) {
        try {
            skipDown(15);
            writeText(LIJST_FONT_SIZE, 30, fontPlain, regel.getOmschrijving());
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private static int LIJST_FONT_SIZE = 10;

    private void listFactuurHeader()  {
        try {
            skipDown(15);
            int y = 30;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Nummer");
            y+=50;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Klant");
            y+=80;
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

    private void listFactuur(Factuur factuur)  {
        try {
            skipDown(15);
            int y = 30;
            writeText(LIJST_FONT_SIZE, y, fontPlain, factuur.getFactuurNummer());
            y+=50;
            Contact contact = factuur.getContact();
            writeText(LIJST_FONT_SIZE, y, fontPlain, contact == null ? "" : contact.getNaam());
            y+=80;
            writeText(LIJST_FONT_SIZE, y, fontPlain, factuur.getFactuurDate().toString());
            y+=60;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",factuur.getBedragExBtw()));
            y+=80;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",factuur.getBtw()));
            y+=40;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",factuur.getBedragIncBtw()));
            y+=80;
            String status = "";
            if (factuur.isBetaald() && factuur.getGekoppeldAfschrift()!=null){
                status = "Geboekt";
            }
            if (factuur.isBetaald() && factuur.getGekoppeldAfschrift()==null){
                status = "Betaald";
            }
            if (!factuur.isBetaald()){
                status = "Niet betaald";
            }
            writeText(LIJST_FONT_SIZE, y, fontPlain, status);
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

    private void listRekening(Rekening rekening)  {
        try {
            skipDown(15);
            int y = 30;
            writeText(LIJST_FONT_SIZE, y, fontPlain, rekening.getRekeningNummer());
            y+=50;
            writeText(LIJST_FONT_SIZE, y, fontPlain, rekening.getRekeningDate().toString());
            y+=60;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",rekening.getBedragExBtw()));
            y+=80;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",rekening.getBtw()));
            y+=40;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",rekening.getBedragIncBtw()));
            y+=80;
            String status = "Niet geboekt";
            if (rekening.getGekoppeldAfschrift()!=null){
                status = "Geboekt";
            }
            writeText(LIJST_FONT_SIZE, y, fontPlain, status);

            skipDown(15);
            writeText(LIJST_FONT_SIZE, 30, fontPlain, "klant:"+rekening.getNaam().toString());
            skipDown(15);
            writeText(LIJST_FONT_SIZE, 30, fontPlain, "omschrijving:"+rekening.getOmschrijving().toString());
            skipDown(5);

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }



    private void listDeclaratiesHeader()  {
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
            writeText(LIJST_FONT_SIZE, y, fontBold, "Omschrijving");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void listDeclaraties(Declaratie declaratie)  {
        try {
            skipDown(15);
            int y = 30;
            writeText(LIJST_FONT_SIZE, y, fontPlain, declaratie.getDeclaratieNummer());
            y+=50;
            writeText(LIJST_FONT_SIZE, y, fontPlain, declaratie.getDeclaratieDate().toString());
            y+=60;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",declaratie.getBedragExBtw()));
            y+=80;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",declaratie.getBtw()));
            y+=40;
            writeText(LIJST_FONT_SIZE, y, fontPlain, String.format("%.2f",declaratie.getBedragIncBtw()));
            y+=80;
            writeText(LIJST_FONT_SIZE, y, fontPlain, declaratie.getOmschrijving().toString());

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }



    private void listAfschriftHeader()  {
        try {
            skipDown(15);
            int y = 30;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Nummer");
            y+=50;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Datum");
            y+=60;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Bedrag");
            y+=60;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Type");
            y+=100;
            writeText(LIJST_FONT_SIZE, y, fontBold, "Naam");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private void listAfschrift(Afschrift afschrift)  {
        try {
            skipDown(15);
            int y = 30;
            writeText(LIJST_FONT_SIZE, y, fontPlain, afschrift.getNummer());
            y+=50;
            writeText(LIJST_FONT_SIZE, y, fontPlain, afschrift.getBoekdatum().toString());
            y+=60;
            writeText(LIJST_FONT_SIZE, y, fontPlain, ""+afschrift.getBedrag());
            y+=60;
            String status = "";
            switch (afschrift.getBoekingType()){
                case NONE:
                    status = "Niet gekoppeld";
                    break;
                case FACTUUR:
                    status = "Factuur "+afschrift.getFactuurNummer();
                    break;
                case REKENING:
                    status = "Rekening "+afschrift.getRekeningNummer();
                    break;
                case PRIVE:
                    status = "Prive boeking";
                    break;
            }
            writeText(LIJST_FONT_SIZE, y, fontPlain, status);
            y+=100;
            writeText(LIJST_FONT_SIZE, y, fontPlain, afschrift.getRelatienaam());
            skipDown(15);
            writeText(LIJST_FONT_SIZE, 30, fontPlain, afschrift.getOmschrijving());
            skipDown(5);

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
        writeText(12, 300, font, ":");
        writeText(12, 305, font, titel3);
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
