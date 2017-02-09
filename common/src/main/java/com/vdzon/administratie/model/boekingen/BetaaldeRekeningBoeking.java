package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;
import org.mongodb.morphia.annotations.Id;

public class BetaaldeRekeningBoeking extends Boeking implements BoekingMetRekening {
    private String rekeningNummer;
    private String afschriftNummer;
    @Id
    private String uuid;

    public BetaaldeRekeningBoeking() {
    }

    private BetaaldeRekeningBoeking(Builder builder) {
        rekeningNummer = builder.rekeningNummer;
        afschriftNummer = builder.afschriftNummer;
        uuid = builder.uuid;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BetaaldeRekeningBoeking copy) {
        Builder builder = new Builder();
        builder.rekeningNummer = copy.rekeningNummer;
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
    public String getRekeningNummer() {
        return rekeningNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Betaling van een rekening";
    }

    public static final class Builder {
        private String rekeningNummer;
        private String afschriftNummer;
        private String uuid;

        private Builder() {
        }

        public Builder rekeningNummer(String val) {
            rekeningNummer = val;
            return this;
        }

        public Builder afschriftNummer(String val) {
            afschriftNummer = val;
            return this;
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public BetaaldeRekeningBoeking build() {
            return new BetaaldeRekeningBoeking(this);
        }
    }
}
