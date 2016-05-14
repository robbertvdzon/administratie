package com.vdzon.administratie.contact;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.auth.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.ContactDto;
import com.vdzon.administratie.dto.FactuurDto;
import com.vdzon.administratie.model.Contact;
import com.vdzon.administratie.model.Factuur;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;
import java.util.UUID;

//import com.fasterxml.jackson.datatype.jsr310.JSR310Module;

public class ContactService {

    @Inject
    UserCrud crudService;

    protected Object putContact(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String contactJson = req.body();
            Contact contact = null;
            try {
                ObjectMapper mapper = new ObjectMapper();
                ContactDto contactDto = mapper.readValue(contactJson, ContactDto.class);
                // TODO; dit op een betere plek. Bij een nieuwe contact moet er een uuid komen
                if (contactDto.getUuid() == null) {
                    contactDto.setUuid(getNewUuid());
                }

                contact = contactDto.toContact();
            } catch (JsonParseException e) {
                e.printStackTrace();
                // Hey, you did not send a valid request!
            }

            gebruiker.getDefaultAdministratie().removeContact(contact.getUuid());
            gebruiker.getDefaultAdministratie().addContact(contact);
            crudService.updateGebruiker(gebruiker);
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
        return new SingleAnswer("ok");
    }

    protected Object removeContact(Request req, Response res) throws Exception {
        try {
            String uuid = SessionHelper.getAuthenticatedUserUuid(req);
            Gebruiker gebruiker = crudService.getGebruiker(uuid);
            if (gebruiker == null) {
                res.status(404);
                return new SingleAnswer("not found");
            }
            String contactUuid = req.params(":uuid");
            if ("undefined".equals(contactUuid)) {
                contactUuid = null;
            }
            gebruiker.getDefaultAdministratie().removeContact(contactUuid);
            crudService.updateGebruiker(gebruiker);
            return new SingleAnswer("ok");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }

    }

    private String getNewUuid() {
        return UUID.randomUUID().toString();
    }

}
