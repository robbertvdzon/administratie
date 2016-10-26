package com.vdzon.administratie.model.boekingen;

import com.vdzon.administratie.model.boekingen.relaties.BoekingMetAfschrift;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;
import org.mongodb.morphia.annotations.Id;

public class BetaaldeFactuurBoeking extends Boeking implements BoekingMetFactuur {
    private String factuurNummer;
    private String afschriftNummer;
    @Id
    private String uuid;

    public BetaaldeFactuurBoeking() {
    }

    private BetaaldeFactuurBoeking(Builder builder) {
        factuurNummer = builder.factuurNummer;
        afschriftNummer = builder.afschriftNummer;
        uuid = builder.uuid;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(BetaaldeFactuurBoeking copy) {
        Builder builder = new Builder();
        builder.factuurNummer = copy.factuurNummer;
        builder.afschriftNummer = copy.afschriftNummer;
        builder.uuid = copy.uuid;
        return builder;
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getFactuurNummer() {
        return factuurNummer;
    }

    @Override
    public String getAfschriftNummer() {
        return afschriftNummer;
    }

    @Override
    public String getOmschrijving() {
        return "Betaalde factuur";
    }


    public static final class Builder {
        private String factuurNummer;
        private String afschriftNummer;
        private String uuid;

        private Builder() {
        }

        public Builder factuurNummer(String val) {
            factuurNummer = val;
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

        public BetaaldeFactuurBoeking build() {
            return new BetaaldeFactuurBoeking(this);
        }
    }
}
