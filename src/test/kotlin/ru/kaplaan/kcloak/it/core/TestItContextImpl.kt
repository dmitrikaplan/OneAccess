package ru.kaplaan.kcloak.it.core

import jakarta.servlet.http.Cookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.oidc.AccessTokenResponse
import ru.kaplaan.kcloak.it.steps.OidcSteps
import ru.kaplaan.kcloak.it.steps.UserSteps
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.dto.User
import ru.kaplaan.kcloak.web.dto.UserDto
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Component
class TestItContextImpl : TestItContext {

    @Autowired
    lateinit var userSteps: UserSteps

    @Autowired
    lateinit var oidcSteps: OidcSteps

    private var accessToken: AccessTokenResponse? = null

    private var cookie: Cookie? = null

    override fun getAllUsers(): List<UserDto> {
        return userSteps.findAllUsers(checkNotNull(cookie))
    }

    override fun getAllClients(): Collection<ClientDto> {
        return userSteps.getAllClients(checkNotNull(cookie))
    }

    override fun getAllRoles(): Collection<RoleDto> {
        return userSteps.getAllRoles(checkNotNull(cookie))
    }

    override fun loginViaClientCredentials(clientCredentials: ClientCredentials, errorExpected: Boolean) {
        accessToken = oidcSteps.loginViaClientCredentials(clientCredentials, errorExpected)
    }

    override fun loginViaAuthorizationCodeFlow(clientCredentials: ClientCredentials) {
        accessToken = oidcSteps.loginViaAuthorizationCodeFlow(clientCredentials, checkNotNull(cookie))
    }

    override fun login(
        userCredentials: UserCredentials,
        expectedError: Boolean
    ) {
        cookie = userSteps.login(userCredentials)
    }

    override fun logout() {
        cookie = null
    }


    override fun getOpenIdConfiguration(): OidcProviderConfiguration {
        return oidcSteps.getOpenIdConfiguration()
    }

    override fun assertAccessTokenNotNull() {
        assertNotNull(accessToken)
    }

    override fun assertAccessTokenNull() {
        assertNull(accessToken)
    }

    override fun assertIsAuthorized() {
        assertNotNull(cookie)
    }
}