package com.vdzon.administratie.afschrift.regelParsers;

public interface RegelParser {

    boolean match(String omschrijving);

    String extractNaam(String omschrijving);

    String extractOmschrijving(String omschrijving);
}
