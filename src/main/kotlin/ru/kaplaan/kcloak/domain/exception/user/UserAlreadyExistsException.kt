package ru.kaplaan.kcloak.domain.exception.user

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error
import ru.kaplaan.kcloak.domain.exception.OAuth2Exception

class UserAlreadyExistsException(
    email: String
): OAuth2Exception(
    oauth2Error = OAuth2Error("already exists", "user with email $email already exists", null),
    httpStatus = HttpStatus.BAD_REQUEST
)