package ru.kaplaan.kcloak.it.core.creds

import ru.kaplaan.kcloak.config.properties.SupportedScopes

open class ClientCredentials(
    val clientId: String,
    val clientSecret: String,
    val redirectUri: String,
    private val scopes: Set<SupportedScopes>,
) {

    object CLIENT_WITHOUT_REFRESH_TOKEN_GRANT :
        ClientCredentials("client1", "client1secret", "http://localhost:5000", setOf(SupportedScopes.OPENID))

    object CLIENT1 : ClientCredentials(
        "client2",
        "test-client",
        "http://localhost:5000",
        setOf(SupportedScopes.OPENID)
    )

    fun scopesToString(): String {
        return scopes.joinToString(" ") { it.value }
    }
}