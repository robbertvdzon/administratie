package com.vdzon.administratie.rest

import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.client.Clients
import org.pac4j.core.config.Config
import org.pac4j.core.config.ConfigFactory
import org.pac4j.core.matching.PathMatcher
import org.pac4j.core.profile.CommonProfile
import org.pac4j.oauth.client.FacebookClient
import org.pac4j.sparkjava.DefaultHttpActionAdapter

class AuthConfigFactory : ConfigFactory {

    override fun build(vararg  vars:Any): Config {
        val callbackHost: String = System.getenv("callbackHost") ?: "http://localhost:4567"
        val facebookKey: String = System.getenv("facebookKey") ?: "1891669624438759"
        val facebookPasswd: String = System.getenv("facebookPasswd") ?: "3d9d6bc369072d9b666cc767d3ede7c7"

        val facebookClient = FacebookClient(facebookKey, facebookPasswd)
        val clients = Clients("$callbackHost/callback", facebookClient)
        val config = Config(clients)

        config.addAuthorizer("admin", RequireAnyRoleAuthorizer<CommonProfile>("ROLE_ADMIN"))
        config.addMatcher("excludedPath", PathMatcher().excludeRegex("^/facebook/notprotected$"))
        config.httpActionAdapter = DefaultHttpActionAdapter()
        return config
    }
}
