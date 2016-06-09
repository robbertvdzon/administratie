package com.vdzon.administratie.pdfgenerator;

import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.AdministratieGegevens;
import com.vdzon.administratie.pdfgenerator.overzicht.CalculateOverzicht;
import com.vdzon.administratie.pdfgenerator.overzicht.Overzicht;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.*;
import java.net.URL;

public class GenerateOverzicht {

    private static PDFont fontPlain = PDType1Font.HELVETICA;
    private static PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    private float pos = 0;
    private float pageHeight = 0;
    private float pageWidth = 0;
    private PDPageContentStream page;

    public static void buildPdf(Administratie administratie, String beginDate, String endDate, BufferedOutputStream outputStream) throws IOException {
        new GenerateOverzicht().start(administratie, beginDate, endDate, outputStream);
    }

    private void start(Administratie administratie, String beginDate, String endDate, BufferedOutputStream outputStream) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);
        PDRectangle rect = page1.getMediaBox();
        document.addPage(page1);
        page = new PDPageContentStream(document, page1);

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
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Uitgeschreven facturen", overzicht.facturenTotaalExBtw, overzicht.facturenTotaalBtw, overzicht.belastbaarInkomenIncBtw);
        skipDown(30);
        writeTitle("Uitgaven");
        writeRegelsBetaaldOntvangenRegel(fontBold, "", "Prijs excl.", "Btw", "Prijs incl.");
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Betaalde rekeningen", overzicht.rekeningenTotaalExBtw, overzicht.rekeningenTotaalBtw, overzicht.rekeningenTotaalIncBtw);
        writeRegelsBetaaldOntvangenRegel(fontPlain, "Declaraties", overzicht.declaratiesTotaalExBtw, overzicht.declaratiesTotaalBtw, overzicht.declaratiesTotaalIncBtw);
        skipDown(30);
        writeTitle("Controle");
        writeFieldValue(fontPlain, "Betaalde facturen", overzicht.betaaldeFacturen);
        writeFieldValue(fontPlain, "Onbetaalde facturen", overzicht.onbetaaldeFacturen);
        writeFieldValue(fontPlain, "Betaalde rekeningen", overzicht.betaaldeRekeningen);
        writeFieldValue(fontPlain, "Betaalde declaraties", overzicht.betaaldeDeclaraties);
        writeFieldValue(fontPlain, "Verwacht totaal op rekening bij", overzicht.verwachtTotaalOpRekeningBij);
        skipDown(10);
        writeFieldValue(fontPlain, "Werkelijk op bank bijgeschreven", overzicht.werkelijkOpBankBij);
        writeFieldValue(fontPlain, "Prive boekingen op bank", overzicht.priveOpBankBij);
        writeFieldValue(fontPlain, "Werkelijk totaal voor administratie", overzicht.werkelijkOpBankBijVoorAdministratie);
        skipDown(10);
        writeFieldValue(fontPlain, "Verschil tussen verwacht en werkelijk ontvangen", overzicht.verschilTussenVerwachtEnWerkelijk);
        skipDown(30);
        writeTitle("Totaal belastbaar inkomen");
        writeFieldValue(fontPlain, "Totaal Exclusief BTW", overzicht.belastbaarInkomenExBtw);
        writeFieldValue(fontPlain, "BTW", overzicht.belastbaarInkomenBtw);
        writeFieldValue(fontPlain, "Totaal Inclusief BTW", overzicht.belastbaarInkomenIncBtw);



        page.close();

        PDPage page2 = new PDPage(PDRectangle.A4);
        document.addPage(page2);
        page = new PDPageContentStream(document, page2);

        writeTitle("Pagina2");
        page.close();

        document.save(outputStream);
        document.close();
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

    private void writeRegelsBetaaldOntvangenRegel(PDFont font, String titel1, String titel2, String titel3, String titel4) throws IOException {
        skipDown(15);
        writeText(12, 30, font, titel1);
        writeText(12, 180, font, titel2);
        writeText(12, 280, font, titel3);
        writeText(12, 380, font, titel4);
    }

    private void writeRegelsBetaaldOntvangenRegel(PDFont font, String titel1, double titel2, double titel3, double titel4) throws IOException {
        writeRegelsBetaaldOntvangenRegel(font, titel1, String.format("%.2f", titel2), String.format("%.2f", titel3), String.format("%.2f", titel4));
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

    private float skipDown(float amount) {
        return pos -= amount;
    }

    private void drawLine() throws IOException {
        skipDown(5);
        page.setLineWidth(1);
        page.moveTo(0, pos);
        page.lineTo(pageWidth, pos);
        page.closeAndStroke();
    }

}
