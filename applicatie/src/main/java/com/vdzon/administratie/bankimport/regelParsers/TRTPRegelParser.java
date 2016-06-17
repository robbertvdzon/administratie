package com.vdzon.administratie.bankimport.regelParsers;

public class TRTPRegelParser implements RegelParser {

    //TODO: deze class kan wat mooier

    @Override
    public boolean match(String omschrijving) {
        return omschrijving.startsWith("/TRTP");
    }


    @Override
    public String extractNaam(String omschrijving) {
        String[] split = omschrijving.split("/");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("NAME")) {
                return split[i + 1];
            }
        }
        return "Onbekend";
    }

    @Override
    public String extractOmschrijving(String omschrijving) {
        String[] split = omschrijving.split("/");
        for (int i = 0; i < split.length; i++) {
            if (split[i].equals("REMI")) {
                return split[i + 1];
            }
        }
        return omschrijving;
    }
}
