package ru.kaplaan.kcloak.it.core.creds

open class ClientCredentials(val clientId: String, val clientSecret: String) {

    object CLIENT1: ClientCredentials("client1", "client1secret")
    object CLIENT2: ClientCredentials("client2", "test-client")
}