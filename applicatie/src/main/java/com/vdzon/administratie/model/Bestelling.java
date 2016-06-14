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
//@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor
@Entity("factuur")
public class Bestelling {

    @Id
    private String uuid;
    private String bestellingNummer;
    private String gekoppeldFactuurNummer;
    private LocalDate bestellingDate;
    private Contact contact;
    private List<BestellingRegel> bestellingRegels = new ArrayList<>();
    private double bedragExBtw = 0;
    private double bedragIncBtw = 0;
    private double btw = 0;

    public List<BestellingRegel> getBestellingRegels() {
        return Collections.unmodifiableList(new ArrayList<>(bestellingRegels));
    }

    /*
     * Use a custom all-arg constructor. This because we want to call calculate at the end of the constructor
     */
    @Builder(toBuilder = true)
    Bestelling(String uuid, String bestellingNummer, String gekoppeldFactuurNummer, LocalDate bestellingDate, Contact contact, List<BestellingRegel> bestellingRegels, double bedragExBtw, double bedragIncBtw, double btw) {
        this.uuid = uuid;
        this.bestellingNummer = bestellingNummer;
        this.gekoppeldFactuurNummer = gekoppeldFactuurNummer;
        this.bestellingDate = bestellingDate;
        this.contact = contact;
        this.bestellingRegels = bestellingRegels;
        this.bedragExBtw = bedragExBtw;
        this.bedragIncBtw = bedragIncBtw;
        this.btw = btw;
        calculate();
    }


    private void calculate() {
        bedragExBtw = 0;
        bedragIncBtw = 0;
        btw = 0;
        for (BestellingRegel bestellingRegel : bestellingRegels) {
            bedragExBtw += bestellingRegel.getStuksPrijs() * bestellingRegel.getAantal();
            btw += bedragExBtw * (bestellingRegel.getBtwPercentage() / 100);
            bedragIncBtw += bedragExBtw + btw;
        }
    }

}
