package com.vdzon.administratie.bankimport;


import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.dto.BoekingType;
import com.vdzon.administratie.model.Gebruiker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.function.Function;

public class ImportFromAbnAmroHelper {

    public static final int START_AFSCHRIFT_NUMMER = 1000;

    protected static Afschrift buildAfschrift(NextNummerHolder nextNummerHolder, AfschriftData afschriftData) {
        BigDecimal bedrag = getBedrag(afschriftData.bedragStr);
        LocalDate boekDatum = getBoekDatum(afschriftData.date);
        int nextAfschriftNummer = nextNummerHolder.nextNummer++;
        return Afschrift.newBuilder()
                .uuid(afschriftData.uuid)
                .nummer("" + nextAfschriftNummer)
                .rekening(afschriftData.rekeningNr)
                .omschrijving(afschriftData.oms)
                .relatienaam(afschriftData.naam)
                .boekdatum(boekDatum)
                .bedrag(bedrag)
                .build();
    }

    protected static AfschriftData parseAfschriftData(String line, Function<String, String> extractNaam, Function<String, String> extractOmschrijving) {
        String[] parts = line.split("\t");
        if (parts.length != 8) {
            return null;
        }
        AfschriftData afschriftData = new AfschriftData();
        afschriftData.rekeningNr = parts[0];
        afschriftData.date = parts[2];
        afschriftData.bedragStr = parts[6];
        afschriftData.omschrijving = parts[7];
        afschriftData.naam = extractNaam.apply(afschriftData.omschrijving);
        afschriftData.oms = extractOmschrijving.apply(afschriftData.omschrijving);
        afschriftData.uuid = afschriftData.rekeningNr.trim() + afschriftData.date.trim() + afschriftData.bedragStr + afschriftData.omschrijving.trim() + afschriftData.naam.trim() + afschriftData.oms.trim();
        return afschriftData;
    }

    protected static int findNextAfschriftNummer(Gebruiker gebruiker) {
        return 1 + gebruiker.getDefaultAdministratie().getAfschriften().stream().map(afschrift -> Integer.parseInt(afschrift.getNummer())).max(Comparator.naturalOrder()).orElse(START_AFSCHRIFT_NUMMER);
    }
    public static String findNextKeyword(String s) {
        String[] words = s.split(" ");
        for (String word : words) {
            if (word.endsWith(":")) {
                return word;
            }
        }
        return null;
    }

    protected static Afschrift getBestaandAfschriftMetDezeUuid(Gebruiker gebruiker, String uuid) {
        return gebruiker.getDefaultAdministratie().getAfschriften().stream().filter(afschrift -> afschrift.getUuid().equals(uuid)).findFirst().orElse(null);
    }

    protected static LocalDate getBoekDatum(String date) {
        try {
            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyyMMdd");
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException exc) {
            exc.printStackTrace();
        }
        return LocalDate.now();

    }

    protected static BigDecimal getBedrag(String bedrag) {
        return BigDecimal.valueOf(new Double(bedrag.replace(",", ".")).doubleValue());
    }


}
