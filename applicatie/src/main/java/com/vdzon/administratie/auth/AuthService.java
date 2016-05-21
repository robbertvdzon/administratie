package com.vdzon.administratie.auth;

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
public class AuthService {

    @Inject
    UserCrud userCrud;

    protected Object login(Request req, Response res) throws Exception {
        String username = req.queryParams("username");
        String password = req.queryParams("password");
        Gebruiker gebruiker = userCrud.getGebruikerByUsername(username);
        if (gebruiker == null || !gebruiker.authenticate(password)) {
            res.status(401);
            SessionHelper.removeAuthenticatedUserUuid(req);
            return new SingleAnswer("not authorized");
        }
        SessionHelper.setAuthenticatedUserUuid(req, gebruiker.getUuid());
        return new SingleAnswer("ok");
    }

    protected Object logout(Request req, Response res) throws Exception {
        SessionHelper.removeAuthenticatedUserUuid(req);
        return new SingleAnswer("ok");
    }

    protected Object getCurrentAdministratie(Request req, Response res) throws Exception {
        String uuid = SessionHelper.getAuthenticatedUserUuid(req);
        if (uuid == null) {
            res.status(404);
            return new SingleAnswer("not found");
        }
        Gebruiker gebruiker = userCrud.getGebruiker(uuid);
        List<GebruikerDto> gebruikers = userCrud.getAllGebruikers().stream().map((user) -> new GebruikerDto(user)).collect(Collectors.<GebruikerDto>toList());
        AdministratieDto administratie = new AdministratieDto(userCrud.getGebruiker(uuid).getDefaultAdministratie());
        return new GuiDataDto(gebruikers, administratie, new GebruikerDto(gebruiker));

    }

}
