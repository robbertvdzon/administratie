package com.vdzon.administratie.pdfgenerator;

import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.AdministratieGegevens;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.FactuurRegel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.net.URL;

public class GenerateFactuur {

    private static PDFont fontPlain = PDType1Font.HELVETICA;
    private static PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    private float pos = 0;
    private float pageHeight = 0;
    private float pageWidth = 0;
    private PDPageContentStream page;

    public static void buildPdf(Administratie administratie, Factuur factuur, BufferedOutputStream outputStream) throws IOException {
        new GenerateFactuur().start(administratie, factuur, outputStream);
    }

    private void start(Administratie administratie, Factuur factuur, BufferedOutputStream outputStream) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);
        PDRectangle rect = page1.getMediaBox();
        document.addPage(page1);
        page = new PDPageContentStream(document, page1);

        pageHeight = rect.getHeight();
        pageWidth = rect.getWidth();
        pos = pageHeight;


        skipDown(25);
        addAnImage(document);
        skipDown(10);
        writeBoldText("Klant factuuradres");
        writeNormalText(factuur.getContact().getNaam());
        writeNormalText(factuur.getContact().getAdres());
        writeNormalText(factuur.getContact().getPostcode() + " " + factuur.getContact().getWoonplaats());
        skipDown(25);
        writeTitle("Factuur");
        drawLine();
        writeNormalText("Factuurnummer " + factuur.getFactuurNummer() + "    " + "Factuurdatum: " + factuur.getFactuurDate());
        drawLine();
        skipDown(10);
        writeRegels(fontBold, "Aantal", "Omschrijving", "Prijs", "Btw", "Totaal ex");
        for (FactuurRegel factuurRegel : factuur.getFactuurRegels()) {
            writeRegels(fontPlain, "" + factuurRegel.getAantal(), "" + factuurRegel.getOmschrijving(), "" + factuurRegel.getStuksPrijs(), "" + factuurRegel.getBtwPercentage(), "" + factuurRegel.getStuksPrijs() * factuurRegel.getAantal());
        }
        skipDown(10);
        drawLine();
        skipDown(10);
        writeTabel(fontPlain, "Bedrag exclusief BTW", "" + String.format("%.2f", factuur.getBedragExBtw()));
        writeTabel(fontPlain, "BTW", "" + factuur.getBtw());
        writeTabel(fontPlain, "Bedrag inclusief BTW", "" + String.format("%.2f", factuur.getBedragIncBtw()));
        skipDown(10);
        drawLine();
        skipDown(10);
        writeNormalText("Bij betaling gaarne factuurnummer vermelden.");
        skipDown(10);

        AdministratieGegevens administratieGegevens = administratie.getAdministratieGegevens();
        if (administratieGegevens != null) {
            writeTabel(fontPlain, "ABN-Amro rekeningnummer", administratieGegevens.getRekeningNummer());
            writeTabel(fontPlain, "BTW-nr", administratieGegevens.getBtwNummer());
            writeTabel(fontPlain, "Handelsregister", administratieGegevens.getHandelsRegister());
            writeTabel(fontPlain, "Adres", administratieGegevens.getAdres());
            writeTabel(fontPlain, "", administratieGegevens.getPostcode() + " " + administratieGegevens.getWoonplaats());
        }

        page.close();

        document.save(outputStream);
        document.close();
    }

    private void addAnImage(PDDocument document) {
        try {
            URL url = new URL("http://www.vdzon.com/robbertlogo.png");
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

    private void writeRegels(PDFont font, String titel1, String titel2, String titel3, String titel4, String titel5) throws IOException {
        skipDown(15);
        writeText(12, 30, font, titel1);
        writeText(12, 80, font, titel2);
        writeText(12, 300, font, titel3);
        writeText(12, 400, font, titel4);
        writeText(12, 500, font, titel5);
    }


    private void writeTabel(PDFont font, String titel1, String titel2) throws IOException {
        skipDown(15);
        writeText(12, 30, font, titel1);
        writeText(12, 190, font, titel1 == null || titel1.length() == 0 ? "" : ":");
        writeText(12, 200, font, titel2);
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
