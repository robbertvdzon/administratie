package com.vdzon.administratie.bankimport.regelParsers;

public interface RegelParser {

    boolean match(String omschrijving);

    String extractNaam(String omschrijving);

    String extractOmschrijving(String omschrijving);
}
