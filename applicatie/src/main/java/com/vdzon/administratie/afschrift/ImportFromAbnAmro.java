package com.vdzon.administratie.afschrift;


import com.vdzon.administratie.afschrift.regelParsers.DefaultRegelParser;
import com.vdzon.administratie.afschrift.regelParsers.RegelParser;
import com.vdzon.administratie.afschrift.regelParsers.SepaRegelParser;
import com.vdzon.administratie.afschrift.regelParsers.TRTPRegelParser;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Gebruiker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.vdzon.administratie.afschrift.ImportFromAbnAmroHelper.*;

public class ImportFromAbnAmro {


    public List<Afschrift> parseFile(Path out, Gebruiker gebruiker) {
        NextNummerHolder nextNummerHolder = new NextNummerHolder(findNextAfschriftNummer(gebruiker));
        try (Stream<String> stream = Files.lines(out)) {
            return stream.map(line -> parseLine(line, gebruiker, nextNummerHolder)).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Afschrift parseLine(String line, Gebruiker gebruiker, NextNummerHolder nextNummerHolder) {
        AfschriftData afschriftData = parseAfschriftData(line, str -> extractNaam(str), str -> extractOmschrijving(str));
        if (afschriftData == null) {
            return null;
        }
        Afschrift bestaandAfschrift = getBestaandAfschriftMetDezeUuid(gebruiker, afschriftData.uuid);
        if (bestaandAfschrift != null) {
            return null;
        }
        return buildAfschrift(nextNummerHolder, afschriftData);
    }

    private String extractNaam(String omschrijving) {
        RegelParser regel = getRegelParsers().stream().filter(parser -> parser.match(omschrijving)).findFirst().orElse(new DefaultRegelParser());
        return regel.extractNaam(omschrijving);
    }

    private String extractOmschrijving(String omschrijving) {
        RegelParser regel = getRegelParsers().stream().filter(parser -> parser.match(omschrijving)).findFirst().orElse(new DefaultRegelParser());
        return regel.extractOmschrijving(omschrijving);
    }

    private List<RegelParser> getRegelParsers() {
        List<RegelParser> parsers = new ArrayList<>();
        parsers.add(new SepaRegelParser());
        parsers.add(new TRTPRegelParser());
        return parsers;
    }
}
