package com.vdzon.administratie.rest.contact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vdzon.administratie.util.SessionHelper;
import com.vdzon.administratie.crud.UserCrud;
import com.vdzon.administratie.dto.ContactDto;
import com.vdzon.administratie.model.Contact;
import com.vdzon.administratie.model.Gebruiker;
import com.vdzon.administratie.util.SingleAnswer;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

public class ContactService {

    @Inject
    UserCrud crudService;

    protected Object putContact(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String contactJson = req.body();
        Contact contact = null;
        ObjectMapper mapper = new ObjectMapper();
        ContactDto contactDto = mapper.readValue(contactJson, ContactDto.class);
        contact = contactDto.toContact();

        gebruiker.getDefaultAdministratie().removeContact(contact.getUuid());
        gebruiker.getDefaultAdministratie().addContact(contact);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

    protected Object removeContact(Request req, Response res) throws Exception {
        Gebruiker gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, crudService);
        String contactUuid = req.params(":uuid");
        if ("undefined".equals(contactUuid)) {
            contactUuid = null;
        }
        gebruiker.getDefaultAdministratie().removeContact(contactUuid);
        crudService.updateGebruiker(gebruiker);
        return new SingleAnswer("ok");
    }

}