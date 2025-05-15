package ru.kaplaan.kcloak.it.tests

import org.junit.Assert.*
import org.junit.Test
import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.SupportedGrantType
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import ru.kaplaan.kcloak.it.core.TestIt
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.randomName
import ru.kaplaan.kcloak.it.core.randomPassword
import ru.kaplaan.kcloak.it.core.randomUUID

class ClientIT : TestIt() {


    @Test
    fun `admin can get all clients`() = testIt {
        login(UserCredentials.SADMIN)
        val clients = getAllClients()
        assertTrue(clients.isNotEmpty())
    }

    @Test
    fun `basic user can't get all clients`() = testIt {
        login(UserCredentials.USER1)
        val clients = getAllClients(httpStatus = HttpStatus.FORBIDDEN)
        assertTrue(clients.isEmpty())
    }

    @Test
    fun `not registered user can't get all clients`() = testIt {
        login(UserCredentials(randomName(), randomPassword()), isErrorExpected = true)
        val clients = getAllClients(httpStatus = HttpStatus.FORBIDDEN)
        assertTrue(clients.isEmpty())
    }

    @Test
    fun `admin can create and update client`() = testIt {
        login(UserCredentials.SADMIN)
        assertIsAuthorized()
        val client = OneAccessClient(
            clientId = randomUUID(),
            clientSecret = randomUUID(),
            clientName = randomName(),
            clientAuthenticationMethods = hashSetOf(ClientAuthenticationMethod.CLIENT_SECRET_POST),
            authorizationGrantTypes = hashSetOf(SupportedGrantType.AUTHORIZATION_CODE),
            redirectUris = hashSetOf("http://localhost:5000"),
            scopes = hashSetOf(SupportedScopes.OPENID)
        )

        val clientFromBackend = createClient(
            oneAccessClient = client
        ) {
            checkNotNull(this)
            assertEquals(client.clientId, clientId)
            assertEquals(client.clientSecret, clientSecret)
            assertEquals(client.clientName, clientName)
            assertEquals(client.clientAuthenticationMethods, clientAuthenticationMethods)
            assertEquals(client.authorizationGrantTypes, authorizationGrantTypes)
            assertEquals(client.redirectUris, redirectUris)
            assertEquals(client.scopes, scopes)
            assertEquals(client.tokenSettings, tokenSettings)
            assertEquals(client.clientSettings, clientSettings)
        }

        val newScopes = client.scopes + SupportedScopes.PROFILE
        val clientId = checkNotNull(clientFromBackend?.clientId)

        updateClient(
            oneAccessClient = OneAccessClient(
                clientId = client.clientId,
                clientSecret = client.clientSecret,
                clientName = client.clientName,
                clientAuthenticationMethods = hashSetOf(ClientAuthenticationMethod.CLIENT_SECRET_POST),
                authorizationGrantTypes = hashSetOf(SupportedGrantType.AUTHORIZATION_CODE),
                redirectUris = hashSetOf("http://localhost:5000"),
                scopes = newScopes
            ),
            clientId = clientId
        ) {
            checkNotNull(this)
            assertEquals(client.clientId, clientId)
            assertEquals(client.clientSecret, clientSecret)
            assertEquals(client.clientName, clientName)
            assertEquals(client.clientAuthenticationMethods, clientAuthenticationMethods)
            assertEquals(client.authorizationGrantTypes, authorizationGrantTypes)
            assertEquals(client.redirectUris, redirectUris)
            assertEquals(newScopes, scopes)
            assertEquals(client.tokenSettings, tokenSettings)
            assertEquals(client.clientSettings, clientSettings)
        }
    }

    @Test
    fun `basic user can't create client`() = testIt {
        login(UserCredentials.USER1)
        assertIsAuthorized()
        val client = OneAccessClient(
            clientId = randomUUID(),
            clientSecret = randomUUID(),
            clientName = randomName(),
            clientAuthenticationMethods = hashSetOf(ClientAuthenticationMethod.CLIENT_SECRET_POST),
            authorizationGrantTypes = hashSetOf(SupportedGrantType.AUTHORIZATION_CODE),
            redirectUris = hashSetOf("http://localhost:5000"),
            scopes = hashSetOf(SupportedScopes.OPENID)
        )

        val clientFromBackend = createClient(
            oneAccessClient = client,
            httpStatus = HttpStatus.FORBIDDEN
        ) {
            assertNull(this)
        }
    }
}