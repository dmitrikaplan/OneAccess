package ru.kaplaan.kcloak.service

import org.springframework.stereotype.Service
import ru.kaplaan.kcloak.config.properties.RealmProperties
import ru.kaplaan.kcloak.config.properties.SupportedGrantTypes
import ru.kaplaan.kcloak.config.properties.SupportedResponseTypesProperties
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import ru.kaplaan.kcloak.domain.exception.RealmNotFoundException
import ru.kaplaan.kcloak.web.dto.JwksCerts
import ru.kaplaan.kcloak.web.dto.OpenIdConfiguration

@Service
class OpenIdService(
    private val realmProperties: RealmProperties,
) {

    fun getOpenIdConfiguration(realm: String, uri: String): OpenIdConfiguration {
        if (realmProperties.name != realm)
            throw RealmNotFoundException(realm)

        return OpenIdConfiguration(
            issuer = "$uri/realms/$realm",
            authorizationEndpoint = "$uri/realms/$realm/protocol/openid-connect/auth",
            tokenEndpoint = "$uri/realms/$realm/protocol/openid-connect/token",
            userInfoEndpoint = "$uri/realms/$realm/protocol/openid-connect/userinfo",
            jwksUri = "$uri/realms/$realm/protocol/openid-connect/certs",
            grantTypeSupported = SupportedGrantTypes.getSupportedGrantTypes(),
            responseTypeSupported = SupportedResponseTypesProperties.getSupportedResponseTypes(),
            scopeSupported = SupportedScopes.getSupportedScopes(),
        )
    }

    fun getJwksCerts(realm: String): JwksCerts {
        return JwksCerts(keys = listOf())
    }
}