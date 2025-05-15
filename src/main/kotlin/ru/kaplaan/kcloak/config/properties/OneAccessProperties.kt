package ru.kaplaan.kcloak.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import java.time.Duration
import java.time.Instant

@ConfigurationProperties("one-access")
data class OneAccessProperties(
    val users: Set<OneAccessUser> = setOf(),
    val roles: Set<OneAccessRole> = setOf(),
    val clients: Set<OneAccessClient> = setOf(),
)

data class OneAccessUser(
    val username: String,
    val email: String,
    val enabled: Boolean,
    val firstName: String,
    val lastName: String,
    val password: String,
    val roles: Set<String> = setOf(),
)

data class OneAccessRole(
    val name: String,
    val permissions: Set<String> = setOf(),
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
    val scopes: Set<SupportedScopes> = setOf(),
    val clientSettings: OneAccessClientSettings = OneAccessClientSettings(),
    val tokenSettings: OneAccessTokenSettings = OneAccessTokenSettings(),
)


data class OneAccessClientSettings(
    val requireAuthorizationConsent: Boolean = false,
    val requireProofKey: Boolean = false,
)

class OneAccessTokenSettings(
    val accessTokenLifespan: Duration = Duration.ofMinutes(15),
    val refreshTokenLifespan: Duration = Duration.ofMinutes(60),
    val authorizationCodeLifespan: Duration = Duration.ofMinutes(15),
    val reuseRefreshTokens: Boolean = false,
)