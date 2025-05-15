package ru.kaplaan.kcloak.config.properties

import org.springframework.security.oauth2.core.oidc.OidcScopes

enum class SupportedScopes(val value: String) {
    READ("read"), WRITE("write"), OPENID(OidcScopes.OPENID),
    EMAIL(OidcScopes.EMAIL), PHONE(OidcScopes.PHONE), PROFILE(OidcScopes.PROFILE),
    ADDRESS(OidcScopes.ADDRESS), OFFLINE_ACCESS("offline_access");

    companion object {

        private fun fromString(value: String): SupportedScopes? {
            return entries.find { it.value.lowercase() == value.lowercase() }
        }

        fun fromString(values: Set<String>): Set<SupportedScopes> {
            return values.mapNotNull { fromString(it) }.toSet()
        }
    }

}