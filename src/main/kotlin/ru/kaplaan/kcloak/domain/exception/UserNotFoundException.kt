package ru.kaplaan.kcloak.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error


class UserNotFoundException(
    description: String,
) : OAuth2Exception(
    oauth2Error = OAuth2Error("not found", description, null),
    httpStatus = HttpStatus.BAD_REQUEST
)