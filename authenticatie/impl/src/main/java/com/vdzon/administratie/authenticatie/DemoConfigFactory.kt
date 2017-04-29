package com.vdzon.administratie.rest

import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.client.Clients
import org.pac4j.core.config.Config
import org.pac4j.core.config.ConfigFactory
import org.pac4j.core.matching.PathMatcher
import org.pac4j.core.profile.CommonProfile
import org.pac4j.oauth.client.FacebookClient

class DemoConfigFactory : ConfigFactory {

    override fun build(): Config {
        val facebookClient = FacebookClient("1891503724455349", "288016a6bd29d32e6dd9ec250e6c6d76")
        val property = System.getenv("callbackHost")
        println("callbackHost prop=$property")
        val callbackHost:String = property ?:"http://localhost:4567"
        println("callbackHost=$callbackHost")

        val clients = Clients("$callbackHost/callback", facebookClient)

        val config = Config(clients)
        config.addAuthorizer("admin", RequireAnyRoleAuthorizer<CommonProfile>("ROLE_ADMIN"))
        config.addAuthorizer("custom", CustomAuthorizer())
        config.addMatcher("excludedPath", PathMatcher().excludeRegex("^/facebook/notprotected$"))
        config.httpActionAdapter = DemoHttpActionAdapter()
        return config
    }
}
