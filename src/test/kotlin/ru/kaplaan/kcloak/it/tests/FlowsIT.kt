package ru.kaplaan.kcloak.it.tests

import org.junit.Test
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.OneAccessTokenSettings
import ru.kaplaan.kcloak.config.properties.SupportedGrantType
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import ru.kaplaan.kcloak.it.core.TestIt
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.oidc.AuthorizationCodeFlowErrorType
import ru.kaplaan.kcloak.it.core.randomName
import ru.kaplaan.kcloak.it.core.randomPassword
import ru.kaplaan.kcloak.it.core.randomUri
import java.time.Duration

class FlowsIT : TestIt() {


    @Test
    fun `successful login via client credentials flow`() = testIt {
        loginViaClientCredentials(ClientCredentials.CLIENT_WITHOUT_REFRESH_TOKEN_GRANT)
        assertAccessTokenNotNull()
    }

    @Test
    fun `failed login via client credentials flow`() = testIt {
        loginViaClientCredentials(
            ClientCredentials(randomName(), randomPassword(), randomUri(), setOf()), isErrorExpected = true
        )
        assertAccessTokenNull()
    }


    @Test
    fun `successful login via authorization code flow via client without refresh_token grant returns only access token`() =
        testIt {
            login(UserCredentials.USER1)
            loginViaAuthorizationCodeFlow(ClientCredentials.CLIENT_WITHOUT_REFRESH_TOKEN_GRANT)
            assertAccessTokenNotNull()
            assertRefreshTokenNull()
        }

    @Test
    fun `successful login via authorization code flow via client with refresh_token grant returns refresh_token`() =
        testIt {
            login(UserCredentials.USER1)
            loginViaAuthorizationCodeFlow(ClientCredentials.CLIENT1)
            assertAccessTokenNotNull()
            assertRefreshTokenNotNull()
        }

    @Test
    fun `failed login via authorization code flow cause invalid client_id`() = testIt {
        login(UserCredentials.USER1)
        loginViaAuthorizationCodeFlow(
            ClientCredentials(randomName(), randomPassword(), randomUri(), setOf()),
            AuthorizationCodeFlowErrorType.INVALID_CLIENT_ID
        )
        assertAccessTokenNull()
    }

    @Test
    fun `failed login via authorization code flow cause invalid credentials`() = testIt {
        login(UserCredentials.USER1)
        loginViaAuthorizationCodeFlow(
            ClientCredentials(
                ClientCredentials.CLIENT_WITHOUT_REFRESH_TOKEN_GRANT.clientId, randomPassword(), randomUri(), setOf()
            ), AuthorizationCodeFlowErrorType.INVALID_CLIENT_SECRET
        )
        assertAccessTokenNull()
    }

    @Test
    fun `successful access_token update via refresh token flow`() = testIt {
        login(UserCredentials.USER1)
        loginViaAuthorizationCodeFlow(ClientCredentials.CLIENT1)
        updateAccessToken()
        assertAccessTokenNotNull()
        assertRefreshTokenNotNull()
    }

    @Test
    fun `failed access_token update when refresh_token is expired`() = testIt {

        // login for creation of new client with ttl of refresh token 10 seconds
        login(UserCredentials.SADMIN)
        val client = createClient(
            OneAccessClient(
                clientId = randomName(),
                clientSecret = randomPassword(),
                clientName = randomName(),
                clientAuthenticationMethods = setOf(
                    ClientAuthenticationMethod.CLIENT_SECRET_POST, ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                ),
                authorizationGrantTypes = setOf(
                    SupportedGrantType.AUTHORIZATION_CODE, SupportedGrantType.REFRESH_TOKEN
                ),
                redirectUris = setOf("http://localhost:5000"),
                scopes = setOf(SupportedScopes.OPENID),
                tokenSettings = OneAccessTokenSettings(
                    refreshTokenLifespan = Duration.ofSeconds(10),
                ),
            )
        ).let {
            checkNotNull(it)
        }

        val clientCredentials =
            ClientCredentials(client.clientId, client.clientSecret, client.redirectUris.first(), client.scopes)
        logout()

        // login via basic user with new client
        login(UserCredentials.USER1)
        loginViaAuthorizationCodeFlow(clientCredentials)
        assertAccessTokenNotNull()
        assertRefreshTokenNotNull()

        //wait for refresh token was expired
        Thread.sleep(Duration.ofSeconds(10))


        //try to update access token
        updateAccessToken(isErrorExpected = true)
    }


    @Test
    fun `failed login via password authentication flow cause authorization grant type password not supported`() =
        testIt {
            login(UserCredentials.SADMIN)
            val client = createClient(
                OneAccessClient(
                    clientId = randomName(),
                    clientSecret = randomPassword(),
                    clientName = randomName(),
                    clientAuthenticationMethods = setOf(
                        ClientAuthenticationMethod.CLIENT_SECRET_POST, ClientAuthenticationMethod.CLIENT_SECRET_BASIC
                    ),
                    authorizationGrantTypes = setOf(
                        SupportedGrantType.CLIENT_CREDENTIALS
                    ),
                    redirectUris = setOf("http://localhost:5000"),
                    scopes = setOf(SupportedScopes.OPENID),
                    tokenSettings = OneAccessTokenSettings(
                        refreshTokenLifespan = Duration.ofSeconds(10),
                    ),
                )
            ).let {
                checkNotNull(it)
            }

            val clientCredentials =
                ClientCredentials(client.clientId, client.clientSecret, client.redirectUris.first(), client.scopes)
            logout()


            loginViaPasswordFlow(UserCredentials.USER1, clientCredentials, isErrorExpected = true)
            assertAccessTokenNull()
            assertRefreshTokenNull()
        }
}