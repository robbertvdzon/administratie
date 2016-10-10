package com.vdzon.administratie.model;

import lombok.*;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@NoArgsConstructor
@Entity("factuur")
public class Factuur {

    @Id
    private String uuid;
    private String factuurNummer;
    private String gekoppeldeBestellingNummer;
    private LocalDate factuurDate;
    private Contact contact;
    private boolean betaald;
    private List<FactuurRegel> factuurRegels = new ArrayList<>();
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    /*
     * Use a custom all-arg constructor. This because we want to call calculate at the end of the constructor
     */
    @Builder(toBuilder = true)
    public Factuur(String uuid, String factuurNummer, String gekoppeldeBestellingNummer, LocalDate factuurDate, Contact contact, boolean betaald, List<FactuurRegel> factuurRegels, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.factuurNummer = factuurNummer;
        this.gekoppeldeBestellingNummer = gekoppeldeBestellingNummer;
        this.factuurDate = factuurDate;
        this.contact = contact;
        this.betaald = betaald;
        this.factuurRegels = factuurRegels;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
        calculate();
    }

    private void calculate() {
        bedragExBtw = 0;
        bedragIncBtw = 0;
        btw = 0;
        for (FactuurRegel factuurRegel : factuurRegels) {
            double regelBedragEx = factuurRegel.getStuksPrijs() * factuurRegel.getAantal();
            double regelBedragBtw = regelBedragEx * (factuurRegel.getBtwPercentage() / 100);
            double regelBedragInc = regelBedragEx + regelBedragBtw;

            bedragExBtw += regelBedragEx;
            btw += regelBedragBtw;
            bedragIncBtw += regelBedragInc;
        }
    }


}
