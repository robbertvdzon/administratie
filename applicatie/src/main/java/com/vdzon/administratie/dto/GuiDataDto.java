package com.vdzon.administratie.dto;


import com.vdzon.administratie.model.Administratie;
import lombok.*;

import java.util.List;

@ToString
@EqualsAndHashCode
@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GuiDataDto {

    private List<GebruikerDto> gebruikers;
    private AdministratieDto administratie;
    private GebruikerDto huidigeGebruiker;

}
