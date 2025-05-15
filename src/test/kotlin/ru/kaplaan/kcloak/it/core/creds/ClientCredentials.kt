package ru.kaplaan.kcloak.it.core.creds

open class ClientCredentials(val clientId: String, val clientSecret: String, val redirectUri: String) {

    object CLIENT1: ClientCredentials("client1", "client1secret", "http://localhost:5000")
    object CLIENT2: ClientCredentials("client2", "test-client", "http://localhost:5000")
}