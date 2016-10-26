package com.vdzon.administratie.dto;


import java.util.List;

public class GuiDataDto {

    private List<GebruikerDto> gebruikers;
    private AdministratieDto administratie;
    private GebruikerDto huidigeGebruiker;

    private GuiDataDto(Builder builder) {
        gebruikers = builder.gebruikers;
        administratie = builder.administratie;
        huidigeGebruiker = builder.huidigeGebruiker;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilder(GuiDataDto copy) {
        Builder builder = new Builder();
        builder.gebruikers = copy.gebruikers;
        builder.administratie = copy.administratie;
        builder.huidigeGebruiker = copy.huidigeGebruiker;
        return builder;
    }

    public List<GebruikerDto> getGebruikers() {
        return gebruikers;
    }

    public AdministratieDto getAdministratie() {
        return administratie;
    }

    public GebruikerDto getHuidigeGebruiker() {
        return huidigeGebruiker;
    }

    public static final class Builder {
        private List<GebruikerDto> gebruikers;
        private AdministratieDto administratie;
        private GebruikerDto huidigeGebruiker;

        private Builder() {
        }

        public Builder gebruikers(List<GebruikerDto> val) {
            gebruikers = val;
            return this;
        }

        public Builder administratie(AdministratieDto val) {
            administratie = val;
            return this;
        }

        public Builder huidigeGebruiker(GebruikerDto val) {
            huidigeGebruiker = val;
            return this;
        }

        public GuiDataDto build() {
            return new GuiDataDto(this);
        }
    }
}
