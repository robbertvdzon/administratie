package com.vdzon.administratie.afschrift.regelParsers;

import static com.vdzon.administratie.afschrift.ImportFromAbnAmroHelper.findNextKeyword;

public class SepaRegelParser implements RegelParser {

    //TODO: deze class kan wat mooier

    @Override
    public boolean match(String omschrijving) {
        return omschrijving.startsWith("SEPA");
    }


    @Override
    public String extractNaam(String omschrijving) {
        int pos = omschrijving.indexOf("Naam:");
        int posStart = pos + "Naam:".length();
        if (pos > 0) {
            String nextKeyword = findNextKeyword(omschrijving.substring(posStart));
            int posEnd = posStart + omschrijving.substring(posStart).indexOf(nextKeyword);
            if (nextKeyword == null) {
                return omschrijving.substring(posStart);
            } else {
                return omschrijving.substring(posStart, posEnd - 1);
            }
        }
        return "Onbekend";
    }

    @Override
    public String extractOmschrijving(String omschrijving) {
        int pos = omschrijving.indexOf("Omschrijving:");
        int posStart = pos + "Omschrijving:".length();
        if (pos > 0) {
            String nextKeyword = findNextKeyword(omschrijving.substring(posStart));
            int posEnd = posStart + omschrijving.substring(posStart).indexOf(nextKeyword);
            if (nextKeyword == null) {
                return omschrijving.substring(posStart);
            } else {
                return omschrijving.substring(posStart, posEnd - 1);
            }
        }
        return omschrijving;
    }
}
