package com.vdzon.administratie.rest

import org.pac4j.core.authorization.authorizer.ProfileAuthorizer
import org.pac4j.core.context.WebContext
import org.pac4j.core.exception.HttpAction
import org.pac4j.core.profile.CommonProfile

class CustomAuthorizer : ProfileAuthorizer<CommonProfile>() {

    @Throws(HttpAction::class)
    override fun isAuthorized(context: WebContext, profiles: List<CommonProfile>): Boolean {
        return isAnyAuthorized(context, profiles)
    }

    public override fun isProfileAuthorized(context: WebContext, profile: CommonProfile?): Boolean {
        if (profile == null) {
            return false
        }
        if (profile.username == null) {
            return false
        }
        return profile.username.startsWith("jle")
    }
}
