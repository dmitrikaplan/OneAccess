package ru.kaplaan.kcloak.web.mapper

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import ru.kaplaan.kcloak.web.dto.ClientDto
import java.time.Instant
import java.util.*


fun OneAccessClient.toRegisteredClient(): RegisteredClient =
    RegisteredClient
        .withId(UUID.nameUUIDFromBytes(clientId.toByteArray()).toString())
        .clientSecret(clientSecret)
        .clientId(clientId)
        .clientSecretExpiresAt(clientSecretExpiresAt)
        .clientName(clientName)
        .also {
            clientAuthenticationMethods.forEach { method ->
                it.clientAuthenticationMethod(method)
            }
        }
        .also {
            authorizationGrantTypes.forEach { authorizationGrantType ->
                it.authorizationGrantType(authorizationGrantType)
            }
        }
        .also {
            redirectUris.forEach { redirectUri ->
                it.redirectUri(redirectUri)
            }
        }
        .also {
            postLogoutRedirectUris.forEach { redirectUri ->
                it.postLogoutRedirectUri(redirectUri)
            }
        }
        .also {
            scopes.forEach { scope ->
                it.scope(scope.value)
            }
        }
        .also {
            if(clientSettings != null) {
                it.clientSettings(clientSettings.toClientSettings())
            }
        }
        .also {
            if(tokenSettings != null){
                it.tokenSettings(tokenSettings.toTokenSettings())
            }
        }
        .build()


fun RegisteredClient.toOneAccessClient(): OneAccessClient {
    return OneAccessClient(
        clientId = clientId,
        clientSecret = checkNotNull(clientSecret),
        clientSecretExpiresAt = clientSecretExpiresAt,
        clientName = clientName,
        clientAuthenticationMethods = clientAuthenticationMethods,
        authorizationGrantTypes = authorizationGrantTypes,
        redirectUris = redirectUris,
        scopes = SupportedScopes.fromString(scopes),
        postLogoutRedirectUris = postLogoutRedirectUris,
        clientSettings = clientSettings.toOneAccessClientSettings(),
        tokenSettings = tokenSettings.toOneAccessTokenSettings(),
    )
}

fun RegisteredClient.toClient(): ClientDto {
    return ClientDto(
        clientId = clientId,
        clientSecret = checkNotNull(clientSecret),
        clientSecretExpiresAt = clientSecretExpiresAt,
        clientName = clientName,
        clientAuthenticationMethods = clientAuthenticationMethods,
        authorizationGrantTypes = authorizationGrantTypes,
        redirectUris = redirectUris,
        scopes = SupportedScopes.fromString(scopes),
        postLogoutRedirectUris = postLogoutRedirectUris,
        clientSettings = clientSettings.toOneAccessClientSettings(),
        tokenSettings = tokenSettings.toOneAccessTokenSettings(),
    )
}
