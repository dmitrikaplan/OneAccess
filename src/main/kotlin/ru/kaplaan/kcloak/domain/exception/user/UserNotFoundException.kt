package ru.kaplaan.kcloak.domain.exception.user

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error
import ru.kaplaan.kcloak.domain.exception.OAuth2Exception


class UserNotFoundException(
    userId: Long?,
) : OAuth2Exception(
    oauth2Error = OAuth2Error("not found", "user with id $userId not found", null),
    httpStatus = HttpStatus.BAD_REQUEST
)