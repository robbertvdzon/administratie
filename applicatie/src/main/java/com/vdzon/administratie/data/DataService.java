package com.vdzon.administratie.data;

import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.AdministratieDto;
import com.vdzon.administratie.dto.GebruikerDto;
import com.vdzon.administratie.dto.GuiDataDto;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
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
        GuiDataDto guiDataDto = null;
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            if (uuid == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            Gebruiker gebruiker = userCrud.getGebruiker(uuid);
            List<GebruikerDto> gebruikers = gebruiker.isAdmin() ? userCrud.getAllGebruikers().stream().map((user) -> new GebruikerDto(user)).collect(Collectors.<GebruikerDto>toList()) : null;
            AdministratieDto administratie = new AdministratieDto(userCrud.getGebruiker(uuid).getDefaultAdministratie());
            guiDataDto = new GuiDataDto(gebruikers, administratie, new GebruikerDto(gebruiker));
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        return guiDataDto;

    }

}
