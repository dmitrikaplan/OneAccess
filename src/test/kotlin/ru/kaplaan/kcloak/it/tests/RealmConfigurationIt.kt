package ru.kaplaan.kcloak.it.tests


import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import ru.kaplaan.kcloak.it.core.TestIt

class RealmConfigurationIt : TestIt() {


    @Test
    fun `users from api equals users from realm configuration`() = testIt {

        val expectedOneAccessUsers = listOf(
            OneAccessUser(
                username = "super-admin",
                email = "superadmin@test1.com",
                enabled = true,
                firstName = "super",
                lastName = "admin",
                password = "superadmin",
                roles = setOf("sadmin")
            ),
            OneAccessUser(
                username = "user1-test1",
                email = "user1@test1.com",
                enabled = true,
                firstName = "user1",
                lastName = "test1",
                password = "123",
                roles = setOf("user")
            ),
            OneAccessUser(
                username = "user2-test1",
                email = "user2@test1.com",
                enabled = true,
                firstName = "user2",
                lastName = "test1",
                password = "123",
                roles = setOf("user")
            )
        )
        val actualOneAccessUsers = getAllUsers()
        assertEquals(expectedOneAccessUsers, actualOneAccessUsers)

    }

    @Test
    fun `clients from api equals clients from configuration`() = testIt {
        val expectedClient =
            OneAccessClient(
                clientId = "default",
                clientSecret = "secret",
                clientName = "test-client",
                clientAuthenticationMethods = setOf(ClientAuthenticationMethod.CLIENT_SECRET_JWT),
                authorizationGrantTypes = setOf(AuthorizationGrantType.AUTHORIZATION_CODE),
                redirectUris = setOf("http://localhost:5000"),
                scopes = setOf(SupportedScopes.OPENID, SupportedScopes.MICROPROFILE_JWT)
            )

        val actualClient = getAllClients().first()

        assertEquals(expectedClient.clientId, actualClient.clientId)
        assertEquals(expectedClient.clientSecret, actualClient.clientSecret)
        assertEquals(expectedClient.clientName, actualClient.clientName)
        assertEquals(expectedClient.clientAuthenticationMethods, actualClient.clientAuthenticationMethods)
        assertEquals(expectedClient.authorizationGrantTypes, actualClient.authorizationGrantTypes)
        assertEquals(expectedClient.redirectUris, actualClient.redirectUris)
        assertEquals(expectedClient.scopes, actualClient.scopes)

    }


    @Test
    fun `roles from api equals roles from configuration`() = testIt {
        val expectedRoles = listOf(
            OneAccessRole(
                name = "sadmin",
                permissions = setOf("READ_REALM_USERS", "WRITE_REALM_USERS"),
            ),
            OneAccessRole(
                name = "user",
                permissions = setOf("USER")
            )
        )

        val actualRoles = getAllRoles()
        assertEquals(expectedRoles, actualRoles)
    }


    private fun <T> assertEquals(expected: Collection<T>, actual: Collection<T>) {
        expected.forEach {
            assertTrue(actual.contains(it))
        }
    }
}