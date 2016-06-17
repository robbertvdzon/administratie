package com.vdzon.administratie.pdfgenerator;

import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

public class FactuurPdfData {
    PDFont fontPlain = PDType1Font.HELVETICA;
    PDFont fontBold = PDType1Font.HELVETICA_BOLD;
    float pos = 0;
    float pageHeight = 0;
    float pageWidth = 0;
    PDPageContentStream page;

}
