package com.vdzon.administratie.pdfgenerator;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.*;
import java.net.URL;

public class GeneratePdfHelper {
    public static final int NORMAL_TEXT_SKIPDOWN = 15;
    public static final int TITLE_TEXT_SKIPDOWN = 20;
    public static final int TITLE_FONT_SIZE = 19;
    public static final int NORMAL_FONT_SIZE = 12;
    public static final int SKIPDOWN_LINE = 5;
    public static final int TEXT_POS_LEFT = 30;
    private FactuurPdfData factuurData;

    public GeneratePdfHelper(FactuurPdfData factuurData) {
        this.factuurData = factuurData;
    }

    void addAnImage(PDDocument document, String logoUrl) {
        try {
            saveLogoToFile(logoUrl, "logo.png");
            PDImageXObject ximage = PDImageXObject.createFromFile("logo.png", document);
            // TODO: waarom scale? Kan ik hier geen vast height gebruiken?
            float scale = 0.5f;
            skipDown( ximage.getHeight() * scale);
            factuurData.page.drawImage(ximage, 30, factuurData.pos, ximage.getWidth() * scale, ximage.getHeight() * scale);
        } catch (IOException ioex) {
            System.out.println("No image for you");
        }
    }

    private void saveLogoToFile(String logoUrl, String filename) throws IOException {
        URL url = new URL(logoUrl);
        InputStream in = new BufferedInputStream(url.openStream());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;
        while (-1 != (n = in.read(buf))) {
            out.write(buf, 0, n);
        }
        out.close();
        in.close();
        byte[] response = out.toByteArray();
        FileOutputStream fos = new FileOutputStream(filename);
        fos.write(response);
        fos.close();
    }

    void writeNormalText(String text) throws IOException {
        skipDown(NORMAL_TEXT_SKIPDOWN);
        writeText( NORMAL_FONT_SIZE, factuurData.fontPlain, text);
    }

    void writeBoldText(String text) throws IOException {
        skipDown(NORMAL_TEXT_SKIPDOWN);
        writeText(NORMAL_FONT_SIZE, factuurData.fontBold, text);
    }

    void writeTitle(String text) throws IOException {
        skipDown(TITLE_TEXT_SKIPDOWN);
        writeText(TITLE_FONT_SIZE, factuurData.fontBold, text);
    }

    void writeText(float fontSize, PDFont fontPlain, String text) throws IOException {
        writeText( fontSize, TEXT_POS_LEFT, fontPlain, text);
    }

    void writeText(float fontSize, float x, PDFont fontPlain, String text) throws IOException {
        if (text == null) return;
        factuurData.page.beginText();
        factuurData.page.setFont(fontPlain, fontSize);
        factuurData.page.newLineAtOffset(x, factuurData.pos);
        factuurData.page.showText(text);
        factuurData.page.endText();
    }

    void writeTabel5(PDFont font, TabelCols cols, String titel1, String titel2, String titel3, String titel4, String titel5) throws IOException {
        skipDown(NORMAL_TEXT_SKIPDOWN);
        writeText(NORMAL_FONT_SIZE, cols.getCol(0), font, titel1);
        writeText(NORMAL_FONT_SIZE, cols.getCol(1), font, titel2);
        writeText(NORMAL_FONT_SIZE, cols.getCol(2), font, titel3);
        writeText(NORMAL_FONT_SIZE, cols.getCol(3), font, titel4);
        writeText(NORMAL_FONT_SIZE, cols.getCol(4), font, titel5);
    }

    void writeTabel3(PDFont font, TabelCols cols, String titel1, String titel2, String titel3) throws IOException {
        skipDown(NORMAL_TEXT_SKIPDOWN);
        writeText(NORMAL_FONT_SIZE, cols.getCol(0), font, titel1);
        writeText(NORMAL_FONT_SIZE, cols.getCol(1), font, titel2);
        writeText(NORMAL_FONT_SIZE, cols.getCol(2), font, titel3);
    }

    float skipDown(float amount) {
        return factuurData.pos -= amount;
    }

    void drawLine() throws IOException {
        skipDown(SKIPDOWN_LINE);
        factuurData.page.setLineWidth(1);
        factuurData.page.moveTo(0, factuurData.pos);
        factuurData.page.lineTo(factuurData.pageWidth, factuurData.pos);
        factuurData.page.closeAndStroke();
    }
}