package com.vdzon.administratie.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.vdzon.administratie.model.BoekingenCache;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.FactuurRegel;
import com.vdzon.administratie.model.boekingen.Boeking;
import com.vdzon.administratie.model.boekingen.relaties.BoekingMetFactuur;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FactuurDto {

    private String uuid;
    private String factuurNummer;
    private String gekoppeldeBestellingNummer;
    private String factuurDate;
    private ContactDto klant;
    private List<FactuurRegelDto> factuurRegels;
    private BigDecimal bedragExBtw = BigDecimal.ZERO;
    private BigDecimal bedragIncBtw = BigDecimal.ZERO;
    private BigDecimal btw = BigDecimal.ZERO;
    private List<BoekingDto> boekingen;

    private static DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public FactuurDto() {
    }

    private FactuurDto(Builder builder) {
        uuid = builder.uuid;
        factuurNummer = builder.factuurNummer;
        gekoppeldeBestellingNummer = builder.gekoppeldeBestellingNummer;
        factuurDate = builder.factuurDate;
        klant = builder.klant;
        factuurRegels = builder.factuurRegels;
        bedragExBtw = builder.bedragExBtw;
        bedragIncBtw = builder.bedragIncBtw;
        btw = builder.btw;
        boekingen = builder.boekingen;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(FactuurDto copy) {
        Builder builder = new Builder();
        builder.uuid = copy.uuid;
        builder.factuurNummer = copy.factuurNummer;
        builder.gekoppeldeBestellingNummer = copy.gekoppeldeBestellingNummer;
        builder.factuurDate = copy.factuurDate;
        builder.klant = copy.klant;
        builder.factuurRegels = copy.factuurRegels;
        builder.bedragExBtw = copy.bedragExBtw;
        builder.bedragIncBtw = copy.bedragIncBtw;
        builder.btw = copy.btw;
        builder.boekingen = copy.boekingen;
        return builder;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFactuurNummer() {
        return factuurNummer;
    }

    public String getGekoppeldeBestellingNummer() {
        return gekoppeldeBestellingNummer;
    }

    public String getFactuurDate() {
        return factuurDate;
    }

    public ContactDto getKlant() {
        return klant;
    }

    public List<FactuurRegelDto> getFactuurRegels() {
        return factuurRegels;
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

    public FactuurDto(Factuur factuur, BoekingenCache boekingenCache){
        this.uuid = factuur.getUuid();
        this.factuurNummer = factuur.getFactuurNummer();
        this.gekoppeldeBestellingNummer = factuur.getGekoppeldeBestellingNummer();
        this.factuurDate = factuur.getFactuurDate()==null ? null : factuur.getFactuurDate().format(DATE_FORMATTER);
        this.klant = factuur.getContact()==null ? null : new ContactDto(factuur.getContact());
        this.factuurRegels = toFactuurRegelsDto(factuur.getFactuurRegels());
        this.bedragExBtw = factuur.getBedragExBtw();
        this.bedragIncBtw = factuur.getBedragIncBtw();
        this.btw = factuur.getBtw();
        boekingen = toBoekingenDto(boekingenCache.getBoekingenVanFactuur(factuurNummer), boekingenCache);
    }

    private List<BoekingDto> toBoekingenDto(List<BoekingMetFactuur> boekingen, BoekingenCache boekingenCache) {
        return boekingen == null ? null : boekingen
                .stream()
                .map(boeking -> new BoekingDto((Boeking)boeking))
                .collect(Collectors.toList());
    }

    private static List<FactuurRegelDto> toFactuurRegelsDto(List<FactuurRegel> factuurRegels) {
        return factuurRegels == null ? new ArrayList<>() : factuurRegels
                .stream()
                .map(factuurRegel -> new FactuurRegelDto(factuurRegel))
                .collect(Collectors.toList());
    }

    public Factuur toFactuur() {
        return Factuur
                .newBuilder()
                .factuurNummer(factuurNummer)
                .gekoppeldeBestellingNummer(gekoppeldeBestellingNummer)
                .factuurDate(LocalDate.parse(factuurDate,DATE_FORMATTER))
                .contact(klant == null ? null : klant.toContact())
                .factuurRegels(toFactuurRegels())
                .uuid(uuid)
//                .gekoppeldAfschrift(gekoppeldAfschrift)
                .build();
    }

    private List<FactuurRegel> toFactuurRegels() {
        return factuurRegels == null ? new ArrayList<>() : factuurRegels
                .stream()
                .map(factuurRegelDto -> factuurRegelDto.toFactuurRegel())
                .collect(Collectors.toList());
    }

    public static final class Builder {
        private String uuid;
        private String factuurNummer;
        private String gekoppeldeBestellingNummer;
        private String factuurDate;
        private ContactDto klant;
        private List<FactuurRegelDto> factuurRegels;
        private BigDecimal bedragExBtw;
        private BigDecimal bedragIncBtw;
        private BigDecimal btw;
        private List<BoekingDto> boekingen;

        private Builder() {
        }

        public Builder uuid(String val) {
            uuid = val;
            return this;
        }

        public Builder factuurNummer(String val) {
            factuurNummer = val;
            return this;
        }

        public Builder gekoppeldeBestellingNummer(String val) {
            gekoppeldeBestellingNummer = val;
            return this;
        }

        public Builder factuurDate(String val) {
            factuurDate = val;
            return this;
        }

        public Builder klant(ContactDto val) {
            klant = val;
            return this;
        }

        public Builder factuurRegels(List<FactuurRegelDto> val) {
            factuurRegels = val;
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

        public FactuurDto build() {
            return new FactuurDto(this);
        }
    }
}
