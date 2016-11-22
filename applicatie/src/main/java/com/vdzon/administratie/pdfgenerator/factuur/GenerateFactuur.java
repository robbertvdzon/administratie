package com.vdzon.administratie.pdfgenerator.factuur;

import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.AdministratieGegevens;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.FactuurRegel;
import com.vdzon.administratie.pdfgenerator.pdfutil.PdfData;
import com.vdzon.administratie.pdfgenerator.pdfutil.GeneratePdfHelper;
import com.vdzon.administratie.pdfgenerator.pdfutil.TabelCols;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenerateFactuur {

    public static void buildPdf(Administratie administratie, Factuur factuur, BufferedOutputStream outputStream) throws IOException {
        new GenerateFactuur().start(administratie, factuur, outputStream);
    }

    private void start(Administratie administratie, Factuur factuur, BufferedOutputStream outputStream) throws IOException {

        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);
        PDRectangle rect = page1.getMediaBox();
        document.addPage(page1);

        PdfData pdfData = new PdfData();
        buildFactuurPdfData(document, page1, rect, pdfData);

        AdministratieGegevens administratieGegevens = administratie.getAdministratieGegevens();

        GeneratePdfHelper generatePdfHelper = new GeneratePdfHelper(pdfData);

        printFactuur(factuur, document, pdfData, administratieGegevens, generatePdfHelper);

        pdfData.page.close();

        document.save(outputStream);
        document.close();
    }

    private void printFactuur(Factuur factuur, PDDocument document, PdfData pdfData, AdministratieGegevens administratieGegevens, GeneratePdfHelper generatePdfHelper) throws IOException {
        printLogo(document, administratieGegevens, generatePdfHelper);
        printKlantGegevens(factuur, generatePdfHelper);
        printFactuurHeaderSummary(factuur, generatePdfHelper);
        printFactuurRegels(factuur, pdfData, generatePdfHelper);
        printSeparatorLine(generatePdfHelper);
        printSummeryTotals(factuur, pdfData, generatePdfHelper);
        printSeparatorLine(generatePdfHelper);
        printBetalingsGegevens(factuur, generatePdfHelper);
        printBedrijfGegevens(pdfData, administratieGegevens, generatePdfHelper);
    }

    private void printBedrijfGegevens(PdfData pdfData, AdministratieGegevens administratieGegevens, GeneratePdfHelper generatePdfHelper) throws IOException {
        if (administratieGegevens != null) {
            TabelCols tabelCols = new TabelCols(30,190,200);
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "ABN-Amro rekeningnummer", ":", administratieGegevens.getRekeningNummer());
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "BTW-nr", ":", administratieGegevens.getBtwNummer());
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Handelsregister", ":", administratieGegevens.getHandelsRegister());
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Adres", ":", administratieGegevens.getAdres());
            generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "", "", administratieGegevens.getPostcode() + " " + administratieGegevens.getWoonplaats());
        }
    }

    private void printBetalingsGegevens(Factuur factuur, GeneratePdfHelper generatePdfHelper) throws IOException {
        generatePdfHelper.writeNormalText("Bij betaling gaarne factuurnummer vermelden.");
        generatePdfHelper.skipDown(10);
    }

    private void printSummeryTotals(Factuur factuur, PdfData pdfData, GeneratePdfHelper generatePdfHelper) throws IOException {
        TabelCols tabelCols = new TabelCols(30,190,200);
        generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Bedrag exclusief BTW", ":", "" + String.format("%.2f", factuur.getBedragExBtw()));
        generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "BTW", ":", "" + String.format("%.2f", factuur.getBtw()));
        generatePdfHelper.writeTabel3(pdfData.fontPlain, tabelCols, "Bedrag inclusief BTW", ":", "" + String.format("%.2f", factuur.getBedragIncBtw()));
    }

    private void printSeparatorLine(GeneratePdfHelper generatePdfHelper) throws IOException {
        generatePdfHelper.skipDown(10);
        generatePdfHelper.drawLine();
        generatePdfHelper.skipDown(10);
    }


    private void printFactuurRegels(Factuur factuur, PdfData pdfData, GeneratePdfHelper generatePdfHelper) throws IOException {
        generatePdfHelper.skipDown(10);
        TabelCols tabelCols = new TabelCols(30,80, 300,400,500);
        generatePdfHelper.writeTabel5(pdfData.fontBold, tabelCols, "Aantal", "Omschrijving", "Prijs", "Btw", "Totaal ex");
        for (FactuurRegel factuurRegel : factuur.getFactuurRegels()) {

            List<String> omschrijvingSplitted = new ArrayList<>();
            String remain = factuurRegel.getOmschrijving();
            while (remain.length()>0){
                String text = findTextWithMaxWidth(pdfData.fontPlain, remain, 1500, GeneratePdfHelper.NORMAL_FONT_SIZE);
                omschrijvingSplitted.add(text);
                remain = remain.substring(text.length());
            }

            boolean first = true;
            for (String omschrijving: omschrijvingSplitted){
                if (first){
                    generatePdfHelper.writeTabel5(pdfData.fontPlain, tabelCols, "" + factuurRegel.getAantal(), "" + omschrijving, "" + factuurRegel.getStuksPrijs(), "" + factuurRegel.getBtwPercentage(), "" + String.format("%.2f", factuurRegel.getStuksPrijs().doubleValue() * factuurRegel.getAantal().doubleValue()));
                    first = false;
                }
                else {
                    generatePdfHelper.writeTabel5(pdfData.fontPlain, tabelCols, "" , omschrijving, "" , "", "" );
                }
            }
        }
    }

    private String findTextWithMaxWidth(PDFont fontPlain, String remain, int maxWith, int fontSize) {
        for (int nr=0; nr<remain.length(); nr++){
            String test = remain.substring(0,nr);
            try {
                if ((fontPlain.getStringWidth(test)/fontSize)>maxWith){
                    return remain.substring(0,nr-1);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return remain;
    }

    private void printFactuurHeaderSummary(Factuur factuur, GeneratePdfHelper generatePdfHelper) throws IOException {
        generatePdfHelper.skipDown(25);
        generatePdfHelper.writeTitle("Factuur");
        generatePdfHelper.drawLine();
        generatePdfHelper.writeNormalText("Factuurnummer " + factuur.getFactuurNummer() + "    " + "Factuurdatum: " + factuur.getFactuurDate());
        generatePdfHelper.drawLine();
    }

    private void printKlantGegevens(Factuur factuur, GeneratePdfHelper generatePdfHelper) throws IOException {
        generatePdfHelper.skipDown(10);
        generatePdfHelper.writeBoldText("Klant factuuradres");
        generatePdfHelper.writeNormalText(factuur.getContact().getNaam());
        generatePdfHelper.writeNormalText(factuur.getContact().getTenNameVan());
        generatePdfHelper.writeNormalText(factuur.getContact().getAdres());
        generatePdfHelper.writeNormalText(factuur.getContact().getPostcode() + " " + factuur.getContact().getWoonplaats());
    }

    private void printLogo(PDDocument document, AdministratieGegevens administratieGegevens, GeneratePdfHelper generatePdfHelper) {
        generatePdfHelper.skipDown(25);
        if (administratieGegevens != null) {
            generatePdfHelper.addAnImage(document, administratieGegevens.getLogoUrl());
        }
    }

    private void buildFactuurPdfData(PDDocument document, PDPage page1, PDRectangle rect, PdfData pdfData) throws IOException {
        pdfData.page = new PDPageContentStream(document, page1);
        pdfData.pageHeight = rect.getHeight();
        pdfData.pageWidth = rect.getWidth();
        pdfData.pos = pdfData.pageHeight;
    }

}
