package com.vdzon.administratie.data

import com.vdzon.administratie.util.SessionHelper
import com.vdzon.administratie.crud.UserCrud
import com.vdzon.administratie.dto.AdministratieDto
import com.vdzon.administratie.dto.GebruikerDto
import com.vdzon.administratie.dto.GuiDataDto
import com.vdzon.administratie.model.Gebruiker
import spark.Request
import spark.Response

import javax.inject.Inject
import javax.inject.Singleton
import java.util.stream.Collectors

@Singleton
class DataService {

    @Inject
    lateinit internal var userCrud: UserCrud

    @Throws(Exception::class)
    fun loadData(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenExceptin(req, userCrud)
        val gebruikers = if (gebruiker.isAdmin!!) userCrud.allGebruikers.map{ user -> GebruikerDto.toDto(user) } else null
        val administratie = AdministratieDto.toDto(gebruiker.defaultAdministratie)
        return GuiDataDto(gebruikers, administratie, GebruikerDto.toDto(gebruiker))
    }

}
