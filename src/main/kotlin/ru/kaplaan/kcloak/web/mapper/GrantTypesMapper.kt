package ru.kaplaan.kcloak.web.mapper

import org.springframework.security.oauth2.core.AuthorizationGrantType
import ru.kaplaan.kcloak.config.properties.SupportedGrantType


fun Set<SupportedGrantType>.toAuthorizationGrantTypes(): Set<AuthorizationGrantType> {
    return this.map { it.toAuthorizationGrantType() }.toSet()
}


fun SupportedGrantType.toAuthorizationGrantType(): AuthorizationGrantType {
    return when (this) {
        SupportedGrantType.AUTHORIZATION_CODE -> AuthorizationGrantType.AUTHORIZATION_CODE
        SupportedGrantType.CLIENT_CREDENTIALS -> AuthorizationGrantType.CLIENT_CREDENTIALS
        SupportedGrantType.REFRESH_TOKEN -> AuthorizationGrantType.REFRESH_TOKEN
    }
}

fun Set<AuthorizationGrantType>.toSupportedGrantTypes(): Set<SupportedGrantType> {
    return this.map { it.toSupportedGrantType() }.toSet()
}

fun AuthorizationGrantType.toSupportedGrantType(): SupportedGrantType {
    return when (this) {
        AuthorizationGrantType.AUTHORIZATION_CODE -> SupportedGrantType.AUTHORIZATION_CODE
        AuthorizationGrantType.REFRESH_TOKEN -> SupportedGrantType.REFRESH_TOKEN
        AuthorizationGrantType.CLIENT_CREDENTIALS -> SupportedGrantType.CLIENT_CREDENTIALS
        else -> throw IllegalStateException()
    }
}