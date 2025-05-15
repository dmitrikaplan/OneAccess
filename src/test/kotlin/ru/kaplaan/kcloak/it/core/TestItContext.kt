package ru.kaplaan.kcloak.it.core

import org.springframework.http.HttpStatus
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.dto.UserDto


interface TestItContext {

    fun loginViaClientCredentials(clientCredentials: ClientCredentials, errorExpected: Boolean = false)

    fun loginViaAuthorizationCodeFlow(clientCredentials: ClientCredentials)

    fun login(userCredentials: UserCredentials, isErrorExpected: Boolean = false)

    fun logout()

    fun assertAccessTokenNotNull()

    fun assertAccessTokenNull()

    fun assertIsAuthorized()

    fun findClient(clientId: Long, httpStatus: HttpStatus = HttpStatus.OK): ClientDto?

    fun getAllClients(httpStatus: HttpStatus = HttpStatus.OK): Collection<ClientDto>

    fun createClient(oneAccessClient: OneAccessClient, httpStatus: HttpStatus = HttpStatus.OK, assertions: (ClientDto?.() -> Unit)? = null): ClientDto?

    fun updateClient(oneAccessClient: OneAccessClient, clientId: String, httpStatus: HttpStatus = HttpStatus.OK, assertions: (ClientDto?.() -> Unit)? = null): ClientDto?

    // users

    fun findUser(userId: Long, httpStatus: HttpStatus = HttpStatus.OK): UserDto?

    fun getAllUsers(httpStatus: HttpStatus = HttpStatus.OK): List<UserDto>

    fun createUser(oneAccessUser: OneAccessUser, httpStatus: HttpStatus = HttpStatus.OK, assertions: (UserDto?.() -> Unit)? = null): UserDto?

    fun updateUser(oneAccessUser: OneAccessUser, userId: Long, httpStatus: HttpStatus = HttpStatus.OK, assertions: (UserDto?.() -> Unit)? = null): UserDto?

    //roles
    fun findRole(roleId: Long, httpStatus: HttpStatus = HttpStatus.OK): RoleDto?

    fun getAllRoles(httpStatus: HttpStatus = HttpStatus.OK): Collection<RoleDto>

    fun createRole(oneAccessRole: OneAccessRole, httpStatus: HttpStatus = HttpStatus.OK, assertions: (RoleDto?.() -> Unit)? = null): RoleDto?

    fun updateRole(oneAccessRole: OneAccessRole, roleId: Long, httpStatus: HttpStatus = HttpStatus.OK, assertions: (RoleDto?.() -> Unit)? = null): RoleDto?
}
