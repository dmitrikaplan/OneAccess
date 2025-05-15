package ru.kaplaan.kcloak.it.tests


import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import ru.kaplaan.kcloak.config.properties.OneAccessClientSettings
import ru.kaplaan.kcloak.config.properties.OneAccessTokenSettings
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import ru.kaplaan.kcloak.it.core.TestIt
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.dto.UserDto

class RealmConfigurationIt : TestIt() {


    @Test
    fun `users from api equals users from realm configuration`() = testIt {
        login(UserCredentials.SADMIN)
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
        login(UserCredentials.SADMIN)
        val expectedClientsDto = listOf(
            ClientDto(
                clientId = "client1",
                clientSecret = "client1secret",
                clientName = "test-client",
                clientAuthenticationMethods = hashSetOf(
                    ClientAuthenticationMethod.CLIENT_SECRET_POST,
                    ClientAuthenticationMethod.CLIENT_SECRET_JWT,
                    ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                ),
                authorizationGrantTypes = hashSetOf(
                    AuthorizationGrantType.AUTHORIZATION_CODE,
                    AuthorizationGrantType.REFRESH_TOKEN,
                    AuthorizationGrantType.CLIENT_CREDENTIALS
                ),
                redirectUris = hashSetOf("http://localhost:5000"),
                scopes = hashSetOf(SupportedScopes.OPENID, SupportedScopes.PROFILE),
                postLogoutRedirectUris = hashSetOf("http://localhost:5000"),
                clientSettings = OneAccessClientSettings(),
                tokenSettings = OneAccessTokenSettings()
            ),
            ClientDto(
                clientId = "client2",
                clientSecret = "test-client",
                clientName = "Test Client",
                clientAuthenticationMethods = hashSetOf(
                    ClientAuthenticationMethod.CLIENT_SECRET_POST,
                    ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                ),
                authorizationGrantTypes = hashSetOf(
                    AuthorizationGrantType.AUTHORIZATION_CODE,
                    AuthorizationGrantType.REFRESH_TOKEN,
                    AuthorizationGrantType.CLIENT_CREDENTIALS
                ),
                redirectUris = hashSetOf("http://localhost:5000"),
                scopes = hashSetOf(SupportedScopes.OPENID),
                clientSettings = OneAccessClientSettings(),
                tokenSettings = OneAccessTokenSettings()

            )
        )

        val actualClient = getAllClients()

        assertThat(actualClient).usingRecursiveComparison()
            .isEqualTo(expectedClientsDto)

    }


    @Test
    fun `roles from api equals roles from configuration`() = testIt {
        login(UserCredentials.SADMIN)
        val expectedRoles = listOf(
            RoleDto(
                roleId = 0,
                name = "SADMIN",
                permissions = hashSetOf("READ_USERS", "WRITE_USERS", "READ_CLIENTS", "WRITE_CLIENTS", "READ_SETTINGS", "WRITE_SETTINGS", "WRITE_ROLES", "READ_ROLES"),
            ),
            RoleDto(
                roleId = 0,
                name = "USER",
                permissions = hashSetOf()
            )
        )

        val actualRoles = getAllRoles()

        assertThat(actualRoles).usingRecursiveComparison()
            .ignoringFields("roleId")
            .isEqualTo(expectedRoles);
    }
}