package com.vdzon.administratie.afschrift.regelParsers;

public class DefaultRegelParser implements RegelParser {

    @Override
    public boolean match(String omschrijving) {
        return false;
    }


    @Override
    public String extractNaam(String omschrijving) {
        return "Onbekend";
    }

    @Override
    public String extractOmschrijving(String omschrijving) {
        return "Onbekend";
    }
}
