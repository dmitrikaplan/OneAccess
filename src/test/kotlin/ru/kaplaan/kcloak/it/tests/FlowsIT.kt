package ru.kaplaan.kcloak.it.tests

import org.junit.Test
import org.springframework.security.test.context.support.WithMockUser
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.TestIt
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.randomPassword
import ru.kaplaan.kcloak.it.core.randomUsername

class FlowsIT: TestIt() {


    @Test
    fun `successful login via client credentials flow`() = testIt {
        loginViaClientCredentials(ClientCredentials.CLIENT1)
        assertAccessTokenNotNull()
    }

    @Test
    fun `failed login via client credentials flow`() = testIt {
        loginViaClientCredentials(ClientCredentials(randomUsername(), randomPassword()), errorExpected = true)
        assertAccessTokenNull()
    }


    @Test
    @WithMockUser(username = "superadmin@test1.com", password = "superadmin", authorities = ["READ_USERS"])
    fun `authorization code flow`() = testIt {
        loginViaAuthorizationCodeFlow(ClientCredentials.CLIENT2, UserCredentials.USER1)
    }
}