package ru.kaplaan.kcloak.it.tests

import org.junit.Test
import org.springframework.security.test.context.support.WithMockUser
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.TestIt
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.randomPassword
import ru.kaplaan.kcloak.it.core.randomUri
import ru.kaplaan.kcloak.it.core.randomUsername
import kotlin.test.assertNotNull

class FlowsIT: TestIt() {


    @Test
    fun `successful login via client credentials flow`() = testIt {
        loginViaClientCredentials(ClientCredentials.CLIENT1)
        assertAccessTokenNotNull()
    }

    @Test
    fun `failed login via client credentials flow`() = testIt {
        loginViaClientCredentials(ClientCredentials(randomUsername(), randomPassword(), randomUri()), errorExpected = true)
        assertAccessTokenNull()
    }


    @Test
    fun `authorization code flow`() = testIt {
        login(UserCredentials.USER1)
        loginViaAuthorizationCodeFlow(ClientCredentials.CLIENT2)
        assertAccessTokenNotNull()
    }
}