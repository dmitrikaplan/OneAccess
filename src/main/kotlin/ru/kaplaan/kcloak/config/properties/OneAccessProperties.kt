package ru.kaplaan.kcloak.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import java.time.Instant

@ConfigurationProperties("one-access")
data class OneAccessProperties(
    val enabled: Boolean,
    val accessTokenLifespan: Int,
    val refreshTokenLifespan: Int,
    val users: Set<OneAccessUser> = setOf(),
    val roles: Set<OneAccessRole> = setOf(),
    val clients: Set<OneAccessClient> = setOf()
)

data class OneAccessUser(
    val username: String,
    val email: String,
    val enabled: Boolean,
    val firstName: String,
    val lastName: String,
    val password: String,
    val roles: Set<String> = setOf()
)

data class OneAccessRole(
    val name: String,
    val permissions: Set<String>
)

data class OneAccessClient(
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