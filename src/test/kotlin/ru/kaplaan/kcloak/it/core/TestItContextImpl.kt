package ru.kaplaan.kcloak.it.core

import jakarta.servlet.http.Cookie
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.oidc.TokenResponse
import ru.kaplaan.kcloak.it.core.oidc.AuthorizationCodeFlowErrorType
import ru.kaplaan.kcloak.it.steps.*
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.dto.UserDto
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@Component
class TestItContextImpl : TestItContext {

    @Autowired
    lateinit var authSteps: AuthSteps

    @Autowired
    lateinit var clientSteps: ClientSteps

    @Autowired
    lateinit var oidcSteps: OidcSteps

    @Autowired
    lateinit var roleSteps: RoleSteps

    @Autowired
    lateinit var userSteps: UserSteps

    private var tokens: TokenResponse? = null

    private var cookie: Cookie? = null

    private var currentClient: ClientCredentials? = null

    override fun loginViaAuthorizationCodeFlow(
        clientCredentials: ClientCredentials,
        authorizationCodeFlowErrorType: AuthorizationCodeFlowErrorType?,
    ) {
        tokens = oidcSteps.loginViaAuthorizationCodeFlow(clientCredentials, cookie, authorizationCodeFlowErrorType)
        currentClient = clientCredentials
    }

    override fun loginViaPasswordFlow(
        userCredentials: UserCredentials,
        clientCredentials: ClientCredentials,
        isErrorExpected: Boolean,
    ) {
        tokens = oidcSteps.loginViaPasswordFlow(userCredentials, clientCredentials, isErrorExpected)
        currentClient = clientCredentials
    }

    override fun updateAccessToken(isErrorExpected: Boolean) {
        tokens = oidcSteps.updateAccessToken(
            checkNotNull(currentClient),
            cookie,
            checkNotNull(tokens?.refreshToken),
            isErrorExpected
        )
    }

    override fun loginViaClientCredentials(clientCredentials: ClientCredentials, isErrorExpected: Boolean) {
        tokens = oidcSteps.loginViaClientCredentials(clientCredentials, isErrorExpected)
        currentClient = clientCredentials
    }

    override fun login(
        userCredentials: UserCredentials,
        isErrorExpected: Boolean,
    ) {
        logout()
        cookie = authSteps.login(userCredentials, isErrorExpected)
    }

    override fun logout() {
        cookie = null
    }

    override fun assertAccessTokenNotNull() {
        assertNotNull(tokens?.accessToken)
    }

    override fun assertAccessTokenNull() {
        assertNull(tokens?.accessToken)
    }

    override fun assertRefreshTokenNotNull() {
        assertNotNull(tokens?.refreshToken)
    }

    override fun assertRefreshTokenNull() {
        assertNull(tokens?.refreshToken)
    }

    override fun assertIsAuthorized() {
        assertNotNull(cookie)
    }

    // users
    override fun findUser(userId: Long, httpStatus: HttpStatus): UserDto? {
        return userSteps.findUser(cookie, userId, httpStatus)
    }

    override fun getAllUsers(httpStatus: HttpStatus): List<UserDto> {
        return userSteps.getAllUsers(cookie, httpStatus)
    }

    override fun createUser(
        oneAccessUser: OneAccessUser,
        httpStatus: HttpStatus,
        assertions: (UserDto?.() -> Unit)?,
    ): UserDto? {
        return userSteps.createUser(cookie, oneAccessUser, httpStatus).also {
            if (assertions != null)
                it.assertions()
        }
    }

    override fun updateUser(
        oneAccessUser: OneAccessUser,
        userId: Long,
        httpStatus: HttpStatus,
        assertions: (UserDto?.() -> Unit)?,
    ): UserDto? {
        return userSteps.updateUser(cookie, oneAccessUser, userId, httpStatus)
            .also {
                if (assertions != null)
                    it.assertions()
            }
    }

    // clients
    override fun findClient(clientId: Long, httpStatus: HttpStatus): ClientDto? {
        return clientSteps.findClient(cookie, clientId, httpStatus)
    }

    override fun getAllClients(httpStatus: HttpStatus): Collection<ClientDto> {
        return clientSteps.getAllClients(cookie, httpStatus)
    }

    override fun createClient(
        oneAccessClient: OneAccessClient,
        httpStatus: HttpStatus,
        assertions: (ClientDto?.() -> Unit)?,
    ): ClientDto? {
        return clientSteps.createClient(cookie, oneAccessClient, httpStatus)
            .also {
                if (assertions != null)
                    it.assertions()
            }
    }

    override fun updateClient(
        oneAccessClient: OneAccessClient,
        clientId: String,
        httpStatus: HttpStatus,
        assertions: (ClientDto?.() -> Unit)?,
    ): ClientDto? {
        return clientSteps.updateClient(cookie, oneAccessClient, clientId, httpStatus)
            .also {
                if (assertions != null)
                    it.assertions()
            }
    }

    // roles

    override fun findRole(roleId: Long, httpStatus: HttpStatus): RoleDto? {
        return roleSteps.findRole(cookie, roleId, httpStatus)
    }

    override fun getAllRoles(httpStatus: HttpStatus): Collection<RoleDto> {
        return roleSteps.getAllRoles(cookie, httpStatus)
    }

    override fun createRole(
        oneAccessRole: OneAccessRole,
        httpStatus: HttpStatus,
        assertions: (RoleDto?.() -> Unit)?,
    ): RoleDto? {
        return roleSteps.createRole(cookie, oneAccessRole, httpStatus)
            .also {
                if (assertions != null)
                    it.assertions()
            }
    }

    override fun updateRole(
        oneAccessRole: OneAccessRole,
        roleId: Long,
        httpStatus: HttpStatus,
        assertions: (RoleDto?.() -> Unit)?,
    ): RoleDto? {
        return roleSteps.updateRole(cookie, oneAccessRole, roleId, httpStatus)
            .also {
                if (assertions != null)
                    it.assertions()
            }
    }
}