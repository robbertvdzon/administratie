package com.vdzon.administratie.pdfgenerator.overzicht;

import com.vdzon.administratie.model.Administratie;
import com.vdzon.administratie.model.Afschrift;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.model.boekingen.*;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import com.vdzon.administratie.mongo.BigDecimalConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class BoekingsVerrijker {
    public static List<VerrijkteBoeking> verrijk(Administratie administratie){
        return administratie.getBoekingen()
                .stream()
                .map(boeking->verrijkBoeking(boeking, administratie))
                .collect(Collectors.toList());
    }

    private static VerrijkteBoeking verrijkBoeking(Boeking boeking, Administratie administratie) {
        if (boeking instanceof InkomstenZonderFactuurBoeking){
            return verrijk((InkomstenZonderFactuurBoeking)boeking, administratie);
        }
        if (boeking instanceof BetalingZonderFactuurBoeking){
            return verrijk((BetalingZonderFactuurBoeking)boeking, administratie);
        }
        if (boeking instanceof PriveBetalingBoeking){
            return verrijk((PriveBetalingBoeking)boeking, administratie);
        }
        if (boeking instanceof BetaaldeFactuurBoeking){
            return verrijk((BetaaldeFactuurBoeking)boeking, administratie);
        }
        if (boeking instanceof BetaaldeRekeningBoeking){
            return verrijk((BetaaldeRekeningBoeking)boeking, administratie);
        }

        throw new RuntimeException("Boeking is van onbekend type : "+boeking.getClass().getCanonicalName());
    }


    private static VerrijkteBoeking verrijk(InkomstenZonderFactuurBoeking boeking, Administratie administratie) {
        Afschrift afschift = getAfschift(boeking, administratie);
        VerrijkteBoeking.BOEKINGSTYPE boekingsType = VerrijkteBoeking.BOEKINGSTYPE.INKOMSTEN_ZONDER_FACTUUR;
        BigDecimal boekingsBedrag = afschift.getBedrag();

        return VerrijkteBoeking.newBuilder()
                .afschrift(afschift)
                .afschriftBedrag(afschift.getBedrag())
                .afschriftDate(afschift.getBoekdatum())
                .boeking(boeking)
                .boekingsBedrag(boekingsBedrag)
                .boekingsType(boekingsType)
                .build();
    }

    private static VerrijkteBoeking verrijk(BetalingZonderFactuurBoeking boeking, Administratie administratie) {
        Afschrift afschift = getAfschift(boeking, administratie);
        VerrijkteBoeking.BOEKINGSTYPE boekingsType = VerrijkteBoeking.BOEKINGSTYPE.BETALING_ZONDER_FACTUUR;
        BigDecimal boekingsBedrag = afschift.getBedrag();

        return VerrijkteBoeking.newBuilder()
                .afschrift(afschift)
                .afschriftBedrag(afschift.getBedrag())
                .afschriftDate(afschift.getBoekdatum())
                .boeking(boeking)
                .boekingsBedrag(boekingsBedrag)
                .boekingsType(boekingsType)
                .build();
    }

    private static VerrijkteBoeking verrijk(PriveBetalingBoeking boeking, Administratie administratie) {
        Afschrift afschift = getAfschift(boeking, administratie);
        VerrijkteBoeking.BOEKINGSTYPE boekingsType = VerrijkteBoeking.BOEKINGSTYPE.PRIVE_BETALING;
        BigDecimal boekingsBedrag = afschift.getBedrag();

        return VerrijkteBoeking.newBuilder()
                .afschrift(afschift)
                .afschriftBedrag(afschift.getBedrag())
                .afschriftDate(afschift.getBoekdatum())
                .boeking(boeking)
                .boekingsBedrag(boekingsBedrag)
                .boekingsType(boekingsType)
                .build();
    }

    private static VerrijkteBoeking verrijk(BetaaldeFactuurBoeking boeking, Administratie administratie) {
        Afschrift afschift = getAfschift(boeking, administratie);
        Factuur factuur = getFactuur(boeking, administratie);
        if (factuur == null){
            factuur = Factuur.newBuilder().factuurDate(afschift.getBoekdatum()).build();
        }
        VerrijkteBoeking.BOEKINGSTYPE boekingsType = VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_FACTUUR;
        BigDecimal boekingsBedrag = afschift.getBedrag();
        BigDecimal factuurBedrag = factuur.getBedragIncBtw();
        LocalDate factuurDate = factuur.getFactuurDate();

        return VerrijkteBoeking.newBuilder()
                .afschrift(afschift)
                .afschriftBedrag(afschift.getBedrag())
                .afschriftDate(afschift.getBoekdatum())
                .boeking(boeking)
                .boekingsBedrag(boekingsBedrag)
                .boekingsType(boekingsType)
                .factuur(factuur)
                .factuurBedrag(factuurBedrag)
                .factuurDate(factuurDate)
                .build();
    }

    private static VerrijkteBoeking verrijk(BetaaldeRekeningBoeking boeking, Administratie administratie) {
        Afschrift afschift = getAfschift(boeking, administratie);
        Rekening rekening = getRekening(boeking, administratie);
        VerrijkteBoeking.BOEKINGSTYPE boekingsType = VerrijkteBoeking.BOEKINGSTYPE.BETAALDE_REKENING;
        BigDecimal boekingsBedrag = afschift.getBedrag();

        return VerrijkteBoeking.newBuilder()
                .afschrift(afschift)
                .afschriftBedrag(afschift.getBedrag())
                .afschriftDate(afschift.getBoekdatum())
                .boeking(boeking)
                .boekingsBedrag(boekingsBedrag)
                .boekingsType(boekingsType)
                .rekening(rekening)
                .rekeningBedrag(rekening.getBedragIncBtw())
                .rekeningDate(rekening.getRekeningDate())
                .build();
    }

    private static Rekening getRekening(BoekingMetRekening boeking, Administratie administratie) {
        return administratie.getRekeningen()
                .stream()
                .filter(rekening -> rekening.getRekeningNummer().equals(boeking.getRekeningNummer()))
                .findFirst()
                .orElse(null);
    }

    private static Factuur getFactuur(BoekingMetFactuur boeking, Administratie administratie) {
        return administratie.getFacturen()
                .stream()
                .filter(factuur -> factuur.getFactuurNummer().equals(boeking.getFactuurNummer()))
                .findFirst()
                .orElse(null);
    }

    private static Afschrift getAfschift(BoekingMetAfschrift boeking, Administratie administratie) {
        return administratie.getAfschriften()
                .stream()
                .filter(afschrift -> afschrift.getNummer().equals(boeking.getAfschriftNummer()))
                .findFirst()
                .orElse(null);
    }
}
