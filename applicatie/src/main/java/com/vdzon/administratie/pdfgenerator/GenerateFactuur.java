package com.vdzon.administratie.pdfgenerator;

import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.AdministratieGegevens;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.FactuurRegel;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;

import java.io.*;

public class GenerateFactuur {

    public static void buildPdf(Administratie administratie, Factuur factuur, BufferedOutputStream outputStream) throws IOException {
        new GenerateFactuur().start(administratie, factuur, outputStream);
    }

    private void start(Administratie administratie, Factuur factuur, BufferedOutputStream outputStream) throws IOException {

        PDDocument document = new PDDocument();
        PDPage page1 = new PDPage(PDRectangle.A4);
        PDRectangle rect = page1.getMediaBox();
        document.addPage(page1);

        FactuurPdfData factuurData = new FactuurPdfData();
        buildFactuurPdfData(document, page1, rect, factuurData);

        AdministratieGegevens administratieGegevens = administratie.getAdministratieGegevens();

        GeneratePdfHelper generatePdfHelper = new GeneratePdfHelper(factuurData);

        printFactuur(factuur, document, factuurData, administratieGegevens, generatePdfHelper);

        factuurData.page.close();

        document.save(outputStream);
        document.close();
    }

    private void printFactuur(Factuur factuur, PDDocument document, FactuurPdfData factuurData, AdministratieGegevens administratieGegevens, GeneratePdfHelper generatePdfHelper) throws IOException {
        printLogo(document, administratieGegevens, generatePdfHelper);
        printKlantGegevens(factuur, generatePdfHelper);
        printFactuurHeaderSummary(factuur, generatePdfHelper);
        printFactuurRegels(factuur, factuurData, generatePdfHelper);
        printSeparatorLine(generatePdfHelper);
        printSummeryTotals(factuur, factuurData, generatePdfHelper);
        printSeparatorLine(generatePdfHelper);
        printBetalingsGegevens(factuur, generatePdfHelper);
        printBedrijfGegevens(factuurData, administratieGegevens, generatePdfHelper);
    }

    private void printBedrijfGegevens(FactuurPdfData factuurData, AdministratieGegevens administratieGegevens, GeneratePdfHelper generatePdfHelper) throws IOException {
        if (administratieGegevens != null) {
            TabelCols tabelCols = new TabelCols(30,190,200);
            generatePdfHelper.writeTabel3(factuurData.fontPlain, tabelCols, "ABN-Amro rekeningnummer", ":", administratieGegevens.getRekeningNummer());
            generatePdfHelper.writeTabel3(factuurData.fontPlain, tabelCols, "BTW-nr", ":", administratieGegevens.getBtwNummer());
            generatePdfHelper.writeTabel3(factuurData.fontPlain, tabelCols, "Handelsregister", ":", administratieGegevens.getHandelsRegister());
            generatePdfHelper.writeTabel3(factuurData.fontPlain, tabelCols, "Adres", ":", administratieGegevens.getAdres());
            generatePdfHelper.writeTabel3(factuurData.fontPlain, tabelCols, "", "", administratieGegevens.getPostcode() + " " + administratieGegevens.getWoonplaats());
        }
    }

    private void printBetalingsGegevens(Factuur factuur, GeneratePdfHelper generatePdfHelper) throws IOException {
        if (factuur.isBetaald()){
            generatePdfHelper.writeNormalText("Factuur is reeds betaald.");
        }
        else{
            generatePdfHelper.writeNormalText("Bij betaling gaarne factuurnummer vermelden.");
        }
        generatePdfHelper.skipDown(10);
    }

    private void printSummeryTotals(Factuur factuur, FactuurPdfData factuurData, GeneratePdfHelper generatePdfHelper) throws IOException {
        TabelCols tabelCols = new TabelCols(30,190,200);
        generatePdfHelper.writeTabel3(factuurData.fontPlain, tabelCols, "Bedrag exclusief BTW", ":", "" + String.format("%.2f", factuur.getBedragExBtw()));
        generatePdfHelper.writeTabel3(factuurData.fontPlain, tabelCols, "BTW", ":", "" + String.format("%.2f", factuur.getBtw()));
        generatePdfHelper.writeTabel3(factuurData.fontPlain, tabelCols, "Bedrag inclusief BTW", ":", "" + String.format("%.2f", factuur.getBedragIncBtw()));
    }

    private void printSeparatorLine(GeneratePdfHelper generatePdfHelper) throws IOException {
        generatePdfHelper.skipDown(10);
        generatePdfHelper.drawLine();
        generatePdfHelper.skipDown(10);
    }

    private void printFactuurRegels(Factuur factuur, FactuurPdfData factuurData, GeneratePdfHelper generatePdfHelper) throws IOException {
        generatePdfHelper.skipDown(10);
        TabelCols tabelCols = new TabelCols(30,80, 300,400,500);
        generatePdfHelper.writeTabel5(factuurData.fontBold, tabelCols, "Aantal", "Omschrijving", "Prijs", "Btw", "Totaal ex");
        for (FactuurRegel factuurRegel : factuur.getFactuurRegels()) {
            generatePdfHelper.writeTabel5(factuurData.fontPlain, tabelCols, "" + factuurRegel.getAantal(), "" + factuurRegel.getOmschrijving(), "" + factuurRegel.getStuksPrijs(), "" + factuurRegel.getBtwPercentage(), "" + String.format("%.2f", factuurRegel.getStuksPrijs() * factuurRegel.getAantal()));
        }
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
        generatePdfHelper.writeNormalText(factuur.getContact().getAdres());
        generatePdfHelper.writeNormalText(factuur.getContact().getPostcode() + " " + factuur.getContact().getWoonplaats());
    }

    private void printLogo(PDDocument document, AdministratieGegevens administratieGegevens, GeneratePdfHelper generatePdfHelper) {
        generatePdfHelper.skipDown(25);
        if (administratieGegevens != null) {
            generatePdfHelper.addAnImage(document, administratieGegevens.getLogoUrl());
        }
    }

    private void buildFactuurPdfData(PDDocument document, PDPage page1, PDRectangle rect, FactuurPdfData factuurData) throws IOException {
        factuurData.page = new PDPageContentStream(document, page1);
        factuurData.pageHeight = rect.getHeight();
        factuurData.pageWidth = rect.getWidth();
        factuurData.pos = factuurData.pageHeight;
    }

}
