package ru.kaplaan.kcloak.config.properties

enum class SupportedGrantTypes(val value: String) {
    AUTHORIZATION_CODE("authorization_code"), REFRESH_TOKEN("refresh_token"),
    PASSWORD("password"), CLIENT_CREDENTIALS("client_credentials");

    companion object {
        fun getSupportedGrantTypes(): Set<String> {
            return entries.map { it.value }.toSet()
        }
    }
}