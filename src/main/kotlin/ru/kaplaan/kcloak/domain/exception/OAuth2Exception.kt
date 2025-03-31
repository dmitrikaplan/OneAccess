package ru.kaplaan.kcloak.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error

open class OAuth2Exception(
    val oauth2Error: OAuth2Error,
    val httpStatus: HttpStatus,
): RuntimeException(oauth2Error.toString())