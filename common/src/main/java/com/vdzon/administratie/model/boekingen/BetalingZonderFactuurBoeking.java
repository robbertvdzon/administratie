package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import org.mongodb.morphia.annotations.Id;

public class BetalingZonderFactuurBoeking extends Boeking implements BoekingMetAfschrift {
    private String afschriftNummer;
    @Id
    private String uuid;

    public BetalingZonderFactuurBoeking() {
    }

    private BetalingZonderFactuurBoeking(Builder builder) {
        afschriftNummer = builder.afschriftNummer;
        uuid = builder.uuid;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BetalingZonderFactuurBoeking copy) {
        Builder builder = new Builder();
        builder.afschriftNummer = copy.afschriftNummer;
        builder.uuid = copy.uuid;
        return builder;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getAfschriftNummer() {
        return afschriftNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Betaling waar geen rekening van is";
    }

    public static final class Builder {
        private String afschriftNummer;
        private String uuid;

        private Builder() {
        }

        public Builder afschriftNummer(String val) {
            afschriftNummer = val;
            return this;
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public BetalingZonderFactuurBoeking build() {
            return new BetalingZonderFactuurBoeking(this);
        }
    }
}