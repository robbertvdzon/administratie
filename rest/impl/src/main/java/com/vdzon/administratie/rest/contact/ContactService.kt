package com.vdzon.administratie.rest.contact

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.ContactDto
import com.vdzon.administratie.model.Contact
import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.util.SingleAnswer
import spark.Request
import spark.Response
import javax.inject.Inject

class ContactService {

    @Inject
    lateinit internal var daoService: UserDao

    @Throws(Exception::class)
    fun putContact(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, daoService)
        val contactJson = req.body()
        var contact: Contact? = null
        val mapper = jacksonObjectMapper()
        val contactDto = mapper.readValue(contactJson, ContactDto::class.java)
        contact = contactDto.toContact()

        gebruiker.defaultAdministratie.removeContact(contact.uuid)
        gebruiker.defaultAdministratie.addContact(contact)
        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

    @Throws(Exception::class)
    fun removeContact(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, daoService)
        var contactUuid: String? = req.params(":uuid")
        if ("undefined" == contactUuid) {
            contactUuid = null
        }
        gebruiker.defaultAdministratie.removeContact(contactUuid!!)
        daoService!!.updateGebruiker(gebruiker)
        return SingleAnswer("ok")
    }

}
