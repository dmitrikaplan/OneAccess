package ru.kaplaan.kcloak.web.mapper

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import java.time.Instant


fun OneAccessClient.toRegisteredClient(): RegisteredClient =
    RegisteredClient.withId(clientId)
        .clientSecret(clientSecret)
        .clientId(clientId)
        .clientIdIssuedAt(Instant.now())
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
        .clientSettings(clientSettings)
        .tokenSettings(tokenSettings)
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
        clientSettings = clientSettings,
        tokenSettings = tokenSettings,
    )
}
