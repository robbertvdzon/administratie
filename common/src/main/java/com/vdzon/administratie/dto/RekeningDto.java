package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Rekening;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetRekening;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties
public class RekeningDto {
    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private String uuid;
    private String rekeningNummer;
    private String factuurNummer;
    private String naam;
    private String omschrijving;
    private String rekeningDate;
    private BigDecimal bedragExBtw = BigDecimal.ZERO;
    private BigDecimal bedragIncBtw = BigDecimal.ZERO;
    private BigDecimal btw = BigDecimal.ZERO;
    private List<BoekingDto> boekingen;
    private int maandenAfschrijving = 0;

    public RekeningDto() {
    }

    private RekeningDto(Builder builder) {
        uuid = builder.uuid;
        rekeningNummer = builder.rekeningNummer;
        factuurNummer = builder.factuurNummer;
        naam = builder.naam;
        omschrijving = builder.omschrijving;
        rekeningDate = builder.rekeningDate;
        bedragExBtw = builder.bedragExBtw;
        bedragIncBtw = builder.bedragIncBtw;
        btw = builder.btw;
        boekingen = builder.boekingen;
        maandenAfschrijving = builder.maandenAfschrijving;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(RekeningDto copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.rekeningNummer = copy.rekeningNummer;
        builder.factuurNummer = copy.factuurNummer;
        builder.naam = copy.naam;
        builder.omschrijving = copy.omschrijving;
        builder.rekeningDate = copy.rekeningDate;
        builder.bedragExBtw = copy.bedragExBtw;
        builder.bedragIncBtw = copy.bedragIncBtw;
        builder.btw = copy.btw;
        builder.boekingen = copy.boekingen;
        builder.maandenAfschrijving = copy.maandenAfschrijving;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getRekeningNummer() {
        return rekeningNummer;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public String getNaam() {
        return naam;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getRekeningDate() {
        return rekeningDate;
    }

    public BigDecimal getBedragExBtw() {
        return bedragExBtw;
    }

    public BigDecimal getBedragIncBtw() {
        return bedragIncBtw;
    }

    public BigDecimal getBtw() {
        return btw;
    }

    public List<BoekingDto> getBoekingen() {
        return boekingen;
    }

    public int getMaandenAfschrijving() {
        return maandenAfschrijving;
    }

    public RekeningDto(Rekening rekening, BoekingenCache boekingenCache) {
        this.uuid = rekening.getUuid();
        this.rekeningNummer = rekening.getRekeningNummer();
        this.factuurNummer = rekening.getFactuurNummer();
        this.naam = rekening.getNaam();
        this.omschrijving = rekening.getOmschrijving();
        this.rekeningDate = rekening.getRekeningDate() == null ? null : rekening.getRekeningDate().format(DATE_FORMATTER);
        this.bedragExBtw = rekening.getBedragExBtw();
        this.bedragIncBtw = rekening.getBedragIncBtw();
        this.btw = rekening.getBtw();
        this.boekingen = toBoekingenDto(boekingenCache.getBoekingenVanRekening(rekeningNummer), boekingenCache);
        this.maandenAfschrijving = rekening.getMaandenAfschrijving();
    }

    private List<BoekingDto> toBoekingenDto(List<BoekingMetRekening> boekingen, BoekingenCache boekingenCache) {
        return boekingen == null ? null : boekingen
                .stream()
                .map(boeking -> new BoekingDto((Boeking)boeking))
                .collect(Collectors.toList());
    }

    public Rekening toRekening() {
        return Rekening.newBuilder()
                .uuid(uuid)
                .rekeningNummer(rekeningNummer)
                .factuurNummer(factuurNummer)
                .naam(naam)
                .omschrijving(omschrijving)
                .rekeningDate(LocalDate.parse(rekeningDate, DATE_FORMATTER))
                .bedragExBtw(bedragExBtw)
                .bedragIncBtw(bedragIncBtw)
                .btw(btw)
                .maandenAfschrijving(maandenAfschrijving)
                .build();
    }

    public static final class Builder {
        private String uuid;
        private String rekeningNummer;
        private String factuurNummer;
        private String naam;
        private String omschrijving;
        private String rekeningDate;
        private BigDecimal bedragExBtw;
        private BigDecimal bedragIncBtw;
        private BigDecimal btw;
        private List<BoekingDto> boekingen;
        private int maandenAfschrijving;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder rekeningNummer(String val) {
            rekeningNummer = val;
            return this;
        }

        public Builder factuurNummer(String val) {
            factuurNummer = val;
            return this;
        }

        public Builder naam(String val) {
            naam = val;
            return this;
        }

        public Builder omschrijving(String val) {
            omschrijving = val;
            return this;
        }

        public Builder rekeningDate(String val) {
            rekeningDate = val;
            return this;
        }

        public Builder bedragExBtw(BigDecimal val) {
            bedragExBtw = val;
            return this;
        }

        public Builder bedragIncBtw(BigDecimal val) {
            bedragIncBtw = val;
            return this;
        }

        public Builder btw(BigDecimal val) {
            btw = val;
            return this;
        }

        public Builder boekingen(List<BoekingDto> val) {
            boekingen = val;
            return this;
        }

        public Builder maandenAfschrijving(int val) {
            maandenAfschrijving = val;
            return this;
        }

        public RekeningDto build() {
            return new RekeningDto(this);
        }
    }
}
