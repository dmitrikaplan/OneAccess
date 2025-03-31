package ru.kaplaan.kcloak.config.properties

enum class SupportedScopes(val value: String) {

    OPENID("openid"), EMAIL("email"), PHONE("phone"),
    PROFILE("profile"), ROLES("roles"), MICROPROFILE_JWT("microprofile-jwt");

    companion object {
        fun getSupportedScopes(): Set<String> {
            return entries.map { it.value }.toSet()
        }
    }

}