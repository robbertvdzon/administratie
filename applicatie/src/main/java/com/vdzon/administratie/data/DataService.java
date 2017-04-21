package com.vdzon.administratie.data;

import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AdministratieDto;
import com.vdzon.administratie.dto.GebruikerDto;
import com.vdzon.administratie.dto.GuiDataDto;
import com.vdzon.administratie.model.Gebruiker;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
public class DataService {

    @Inject
    UserCrud userCrud;

    protected Object loadData(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, userCrud);
        List<GebruikerDto> gebruikers = gebruiker.isAdmin() ? userCrud.getAllGebruikers().stream().map((user) -> GebruikerDto.Companion.toDto(user)).collect(Collectors.<GebruikerDto>toList()) : null;
        AdministratieDto administratie = AdministratieDto.Companion.toDto(gebruiker.getDefaultAdministratie());
        return new GuiDataDto(gebruikers,administratie,GebruikerDto.Companion.toDto(gebruiker));
    }

}
