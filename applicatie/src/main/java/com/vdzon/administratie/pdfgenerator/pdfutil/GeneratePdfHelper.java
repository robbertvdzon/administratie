package com.vdzon.administratie.pdfgenerator.pdfutil;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.font.PDFont;
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
    private PdfData pdfData;

    public GeneratePdfHelper(PdfData pdfData) {
        this.pdfData = pdfData;
    }

    public void addAnImage(PDDocument document, String logoUrl) {
        try {
            saveLogoToFile(logoUrl, "logo.png");
            PDImageXObject ximage = PDImageXObject.createFromFile("logo.png", document);
            // TODO: waarom scale? Kan ik hier geen vast height gebruiken?
            float scale = 0.5f;
            skipDown( ximage.getHeight() * scale);
            pdfData.page.drawImage(ximage, 30, pdfData.pos, ximage.getWidth() * scale, ximage.getHeight() * scale);
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

    public void writeNormalText(String text) throws IOException {
        skipDown(NORMAL_TEXT_SKIPDOWN);
        writeText( NORMAL_FONT_SIZE, pdfData.fontPlain, text);
    }

    public void writeBoldText(String text) throws IOException {
        skipDown(NORMAL_TEXT_SKIPDOWN);
        writeText(NORMAL_FONT_SIZE, pdfData.fontBold, text);
    }

    public void writeTitle(String text) throws IOException {
        skipDown(TITLE_TEXT_SKIPDOWN);
        writeText(TITLE_FONT_SIZE, pdfData.fontBold, text);
    }

    public void writeText(float fontSize, PDFont fontPlain, String text) throws IOException {
        writeText( fontSize, TEXT_POS_LEFT, fontPlain, text);
    }

    public void writeText(float fontSize, float x, PDFont fontPlain, String text) throws IOException {
        if (text == null) return;
        pdfData.page.beginText();
        pdfData.page.setFont(fontPlain, fontSize);
        pdfData.page.newLineAtOffset(x, pdfData.pos);
        pdfData.page.showText(text);
        pdfData.page.endText();
    }

    public void writeTabel5(PDFont font, TabelCols cols, String titel1, String titel2, String titel3, String titel4, String titel5) throws IOException {
        skipDown(NORMAL_TEXT_SKIPDOWN);
        writeText(NORMAL_FONT_SIZE, cols.getCol(0), font, titel1);
        writeText(NORMAL_FONT_SIZE, cols.getCol(1), font, titel2);
        writeText(NORMAL_FONT_SIZE, cols.getCol(2), font, titel3);
        writeText(NORMAL_FONT_SIZE, cols.getCol(3), font, titel4);
        writeText(NORMAL_FONT_SIZE, cols.getCol(4), font, titel5);
    }

    public void writeTabel3(PDFont font, TabelCols cols, String titel1, String titel2, String titel3) throws IOException {
        skipDown(NORMAL_TEXT_SKIPDOWN);
        writeText(NORMAL_FONT_SIZE, cols.getCol(0), font, titel1);
        writeText(NORMAL_FONT_SIZE, cols.getCol(1), font, titel2);
        writeText(NORMAL_FONT_SIZE, cols.getCol(2), font, titel3);
    }

    public float skipDown(float amount) {
        return pdfData.pos -= amount;
    }

    public void drawLine() throws IOException {
        skipDown(SKIPDOWN_LINE);
        pdfData.page.setLineWidth(1);
        pdfData.page.moveTo(0, pdfData.pos);
        pdfData.page.lineTo(pdfData.pageWidth, pdfData.pos);
        pdfData.page.closeAndStroke();
    }
}