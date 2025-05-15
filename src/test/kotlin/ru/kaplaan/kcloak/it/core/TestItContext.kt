package ru.kaplaan.kcloak.it.core

import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.oidc.AuthorizationFlow
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.dto.User
import ru.kaplaan.kcloak.web.dto.UserDto


interface TestItContext {

    fun getAllUsers(): List<UserDto>

    fun getAllClients(): Collection<ClientDto>

    fun getAllRoles(): Collection<RoleDto>

    fun loginViaClientCredentials(clientCredentials: ClientCredentials, errorExpected: Boolean = false)

    fun loginViaAuthorizationCodeFlow(clientCredentials: ClientCredentials)

    fun login(userCredentials: UserCredentials, expectedError: Boolean = false)

    fun logout()

    fun getOpenIdConfiguration(): OidcProviderConfiguration

    fun assertAccessTokenNotNull()

    fun assertAccessTokenNull()

    fun assertIsAuthorized()
}
