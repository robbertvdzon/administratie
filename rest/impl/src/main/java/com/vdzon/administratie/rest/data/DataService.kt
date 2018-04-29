package com.vdzon.administratie.rest.data

import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.AdministratieDto
import com.vdzon.administratie.dto.GebruikerDto
import com.vdzon.administratie.dto.GuiDataDto
import com.vdzon.administratie.util.SessionHelper
import spark.Request
import spark.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataService {

    @Inject
    lateinit internal var userDao: UserDao

    @Throws(Exception::class)
    fun loadData(req: Request, res: Response): Any {
        val gebruiker = SessionHelper.getGebruikerOrThowForbiddenException(req, userDao)
        val gebruikers = if (gebruiker.isAdmin!!) userDao.getAllGebruikers().map { user -> GebruikerDto.toDto(user) } else null
        val administratie = AdministratieDto.toDto(gebruiker.defaultAdministratie)
        return GuiDataDto(gebruikers, administratie, GebruikerDto.toDto(gebruiker))
    }

}
