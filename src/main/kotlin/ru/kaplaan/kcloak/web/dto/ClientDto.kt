package ru.kaplaan.kcloak.web.dto

import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import java.time.Instant

data class ClientDto(
    val clientId: String,
    val clientSecret: String,
    val clientSecretExpiresAt: Instant? = null,
    val clientName: String,
    val clientAuthenticationMethods: Set<ClientAuthenticationMethod>,
    val authorizationGrantTypes: Set<AuthorizationGrantType>,
    val redirectUris: Set<String>,
    val postLogoutRedirectUris: Set<String> = setOf(),
    val scopes: Set<SupportedScopes>,
    val clientSettings: ClientSettings = ClientSettings.builder().build(),
    val tokenSettings: TokenSettings = TokenSettings.builder().build(),
)