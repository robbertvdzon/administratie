package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.Declaratie;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@JsonIgnoreProperties
public class DeclaratieDto {

    private String uuid;
    private String declaratieNummer;
    private String omschrijving;
    private String declaratieDate;
    private BigDecimal bedragExBtw = BigDecimal.ZERO;
    private BigDecimal bedragIncBtw = BigDecimal.ZERO;
    private BigDecimal btw = BigDecimal.ZERO;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public DeclaratieDto() {
    }

    private DeclaratieDto(Builder builder) {
        uuid = builder.uuid;
        declaratieNummer = builder.declaratieNummer;
        omschrijving = builder.omschrijving;
        declaratieDate = builder.declaratieDate;
        bedragExBtw = builder.bedragExBtw;
        bedragIncBtw = builder.bedragIncBtw;
        btw = builder.btw;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(DeclaratieDto copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.declaratieNummer = copy.declaratieNummer;
        builder.omschrijving = copy.omschrijving;
        builder.declaratieDate = copy.declaratieDate;
        builder.bedragExBtw = copy.bedragExBtw;
        builder.bedragIncBtw = copy.bedragIncBtw;
        builder.btw = copy.btw;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getDeclaratieNummer() {
        return declaratieNummer;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public String getDeclaratieDate() {
        return declaratieDate;
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

    public DeclaratieDto(Declaratie declaratie) {
        this.uuid = declaratie.getUuid();
        this.declaratieNummer = declaratie.getDeclaratieNummer();
        this.omschrijving = declaratie.getOmschrijving();
        this.declaratieDate = declaratie.getDeclaratieDate()==null ? null : declaratie.getDeclaratieDate().format(DATE_FORMATTER);
        this.bedragExBtw = declaratie.getBedragExBtw();
        this.bedragIncBtw = declaratie.getBedragIncBtw();
        this.btw = declaratie.getBtw();
    }

    public Declaratie toDeclaratie() {
        return Declaratie
                .newBuilder()
                .uuid(uuid)
                .declaratieNummer(declaratieNummer)
                .omschrijving(omschrijving)
                .declaratieDate(LocalDate.parse(declaratieDate,DATE_FORMATTER))
                .bedragExBtw(bedragExBtw)
                .bedragIncBtw(bedragIncBtw)
                .btw(btw)
                .build();
    }

    public static final class Builder {
        private String uuid;
        private String declaratieNummer;
        private String omschrijving;
        private String declaratieDate;
        private BigDecimal bedragExBtw;
        private BigDecimal bedragIncBtw;
        private BigDecimal btw;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder declaratieNummer(String val) {
            declaratieNummer = val;
            return this;
        }

        public Builder omschrijving(String val) {
            omschrijving = val;
            return this;
        }

        public Builder declaratieDate(String val) {
            declaratieDate = val;
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

        public DeclaratieDto build() {
            return new DeclaratieDto(this);
        }
    }
}
