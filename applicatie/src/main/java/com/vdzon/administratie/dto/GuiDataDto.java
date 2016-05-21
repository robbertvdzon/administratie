package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.Administratie;

import java.util.List;

public class GuiDataDto {

    private List<GebruikerDto> gebruikers;
    private AdministratieDto administratie;
    private GebruikerDto huidigeGebruiker;

    public GuiDataDto() {
    }

    public GuiDataDto(List<GebruikerDto> gebruikers, AdministratieDto administratie, GebruikerDto huidigeGebruiker) {
        this.gebruikers = gebruikers;
        this.administratie = administratie;
        this.huidigeGebruiker = huidigeGebruiker;
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
}
