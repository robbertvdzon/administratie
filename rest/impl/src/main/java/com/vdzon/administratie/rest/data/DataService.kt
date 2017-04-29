package com.vdzon.administratie.rest.data

import com.vdzon.administratie.authenticatie.AuthenticationService
import com.vdzon.administratie.database.UserDao
import com.vdzon.administratie.dto.AdministratieDto
import com.vdzon.administratie.dto.GebruikerDto
import com.vdzon.administratie.dto.GuiDataDto
import spark.Request
import spark.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataService {

    @Inject
    lateinit internal var userDao: UserDao

    @Inject
    lateinit internal var athenticationService: AuthenticationService

    @Throws(Exception::class)
    fun loadData(req: Request, res: Response): Any {
        val gebruiker = athenticationService.getGebruikerOrThowForbiddenException(req, res)
        val gebruikers = if (gebruiker.isAdmin!!) userDao.getAllGebruikers().map { user -> GebruikerDto.toDto(user) } else null
        val administratie = AdministratieDto.toDto(gebruiker.defaultAdministratie)
        return GuiDataDto(gebruikers, administratie, GebruikerDto.toDto(gebruiker))
    }

}
