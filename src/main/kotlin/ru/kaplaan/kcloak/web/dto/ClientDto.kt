package ru.kaplaan.kcloak.web.dto

import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import ru.kaplaan.kcloak.config.properties.OneAccessClientSettings
import ru.kaplaan.kcloak.config.properties.OneAccessTokenSettings
import ru.kaplaan.kcloak.config.properties.SupportedGrantType
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import java.time.Instant

data class ClientDto(
    val clientId: String,
    val clientSecret: String,
    val clientSecretExpiresAt: Instant? = null,
    val clientName: String,
    val clientAuthenticationMethods: Set<ClientAuthenticationMethod>,
    val authorizationGrantTypes: Set<SupportedGrantType>,
    val redirectUris: Set<String>,
    val postLogoutRedirectUris: Set<String> = setOf(),
    val scopes: Set<SupportedScopes>,
    val clientSettings: OneAccessClientSettings,
    val tokenSettings: OneAccessTokenSettings,
)