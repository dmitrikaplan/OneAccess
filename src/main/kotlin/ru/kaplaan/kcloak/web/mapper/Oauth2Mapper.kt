package ru.kaplaan.kcloak.web.mapper

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.jooq.JSONB
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.AuthorizationGrantType.*
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.ClientAuthenticationMethod.*
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings


fun Array<String?>.toClientAuthenticationMethodSet(): Set<ClientAuthenticationMethod> {
    return this.mapNotNull {
        when (it) {
            "client_secret_basic" -> CLIENT_SECRET_BASIC
            "client_secret_post" -> CLIENT_SECRET_POST
            "client_secret_jwt" -> CLIENT_SECRET_JWT
            "private_key_jwt" -> PRIVATE_KEY_JWT
            "none" -> NONE
            "tls_client_auth" -> TLS_CLIENT_AUTH
            "self_signed_tls_client_auth" -> SELF_SIGNED_TLS_CLIENT_AUTH
            else -> null
        }
    }.toSet()
}

fun Array<String?>.toAuthorizationGrantTypesSet(): Set<AuthorizationGrantType> {
    return this.mapNotNull {
        when (it) {
            "authorization_code" -> AUTHORIZATION_CODE
            "refresh_token" -> REFRESH_TOKEN
            "client_credentials" -> CLIENT_CREDENTIALS
            "password" -> PASSWORD
            else -> null
        }
    }.toSet()
}

fun JSONB.toClientSettings(): ClientSettings {
    val settings = jacksonObjectMapper().readValue<Map<String, Any>>(this.data())
    return ClientSettings.withSettings(settings).build()
}

fun JSONB.toTokenSettings(): TokenSettings {
    val settings = jacksonObjectMapper().readValue<Map<String, Any>>(this.data())
    return TokenSettings.withSettings(settings).build()
}