package ru.kaplaan.kcloak.config.properties

enum class SupportedScopes(val value: String) {

    OPENID("openid"), EMAIL("email"), PHONE("phone"),
    PROFILE("profile"), ROLES("roles"), MICROPROFILE_JWT("microprofile-jwt");

    companion object {

        private fun fromString(value: String): SupportedScopes? {
            return entries.find { it.value.lowercase() == value.lowercase() }
        }

        fun fromString(values: Set<String>): Set<SupportedScopes> {
            return values.mapNotNull { fromString(it) }.toSet()
        }
    }

}