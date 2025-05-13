package ru.kaplaan.kcloak.it.tests


import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.TestIt
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.dto.UserDto

class RealmConfigurationIt : TestIt() {


    @Test
    fun `users from api equals users from realm configuration`() = testIt {
        //login(UserCredentials.SADMIN)
        assertIsAuthorized()
        val expectedUsers = listOf(
            UserDto(
                id = 0,
                username = "super-admin",
                email = "superadmin@test1.com",
                enabled = true,
                firstName = "super",
                lastName = "admin",
            ),
            UserDto(
                id = 0,
                username = "user1-test1",
                email = "user1@test1.com",
                enabled = true,
                firstName = "user1",
                lastName = "test1",
            ),
            UserDto(
                id = 0,
                username = "user2-test1",
                email = "user2@test1.com",
                enabled = true,
                firstName = "user2",
                lastName = "test1"
            )
        )
        val actualUsers = getAllUsers()
        assertThat(actualUsers).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedUsers);

    }

    @Test
    fun `clients from api equals clients from configuration`() = testIt {
        val expectedClientDto =
            ClientDto(
                clientId = "default",
                clientSecret = "secret",
                clientName = "test-client",
                clientAuthenticationMethods = setOf(ClientAuthenticationMethod.CLIENT_SECRET_JWT),
                authorizationGrantTypes = setOf(AuthorizationGrantType.AUTHORIZATION_CODE),
                redirectUris = setOf("http://localhost:5000"),
                scopes = setOf(SupportedScopes.OPENID, SupportedScopes.READ, SupportedScopes.WRITE),
            )

        val actualClient = getAllClients().first()

        assertEquals(expectedClientDto.clientId, actualClient.clientId)
        assertEquals(expectedClientDto.clientSecret, actualClient.clientSecret)
        assertEquals(expectedClientDto.clientName, actualClient.clientName)
        assertEquals(expectedClientDto.clientAuthenticationMethods, actualClient.clientAuthenticationMethods)
        assertEquals(expectedClientDto.authorizationGrantTypes, actualClient.authorizationGrantTypes)
        assertEquals(expectedClientDto.redirectUris, actualClient.redirectUris)
        assertEquals(expectedClientDto.scopes, actualClient.scopes)

    }


    @Test
    fun `roles from api equals roles from configuration`() = testIt {
        val expectedRoles = listOf(
            RoleDto(
                roleId = 0,
                name = "sadmin",
                permissions = setOf("READ_REALM_USERS", "WRITE_REALM_USERS"),
            ),
            RoleDto(
                roleId = 0,
                name = "user",
                permissions = setOf("USER")
            )
        )

        val actualRoles = getAllRoles()

        assertThat(actualRoles).usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedRoles);
    }

    @Test
    fun `open id configuration`() = testIt {
        val config = getOpenIdConfiguration()
        println(config)
    }


    private fun <T> assertEquals(expected: Collection<T>, actual: Collection<T>) {
        expected.forEach {
            assertTrue(actual.contains(it))
        }
    }
}