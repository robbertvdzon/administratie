package com.vdzon.administratie.pdfgenerator.pdfutil;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class PdfData {
    public PDFont fontPlain = PDType1Font.HELVETICA;
    public PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    public float pos = 0;
    public float pageHeight = 0;
    public float pageWidth = 0;
    public PDPageContentStream page;

}
