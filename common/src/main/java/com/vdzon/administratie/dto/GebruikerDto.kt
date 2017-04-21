package com.vdzon.administratie.dto


import com.vdzon.administratie.model.Gebruiker

class GebruikerDto(
        val uuid: String,
        val name: String,
        val username: String,
        val isAdmin: Boolean) {

    fun cloneGebruikerWithDtoFields(gebruiker: Gebruiker): Gebruiker {
        return gebruiker.copy(
                username=username,
                name=name,
                uuid=uuid,
                isAdmin=isAdmin)
    }


    companion object {

        fun toDto(gebruiker: Gebruiker) = GebruikerDto(
                uuid = gebruiker.uuid,
                name = gebruiker.name,
                username = gebruiker.username,
                isAdmin = gebruiker.isAdmin)
    }
}
