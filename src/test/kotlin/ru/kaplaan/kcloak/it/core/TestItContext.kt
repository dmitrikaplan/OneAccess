package ru.kaplaan.kcloak.it.core

import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.oidc.AuthorizationFlow
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.dto.User


interface TestItContext {

    fun getAllUsers(): Collection<User>

    fun getAllClients(): Collection<ClientDto>

    fun getAllRoles(): Collection<RoleDto>

    fun loginViaClientCredentials(clientCredentials: ClientCredentials, errorExpected: Boolean = false)

    fun loginViaAuthorizationCodeFlow(clientCredentials: ClientCredentials, userCredentials: UserCredentials)

    fun getOpenIdConfiguration(): OidcProviderConfiguration

    fun assertAccessTokenNotNull()

    fun assertAccessTokenNull()

    fun assertIsAuthorized()
}
