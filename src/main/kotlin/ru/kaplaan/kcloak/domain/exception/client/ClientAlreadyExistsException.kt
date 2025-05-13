package ru.kaplaan.kcloak.domain.exception.client

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error
import ru.kaplaan.kcloak.domain.exception.OAuth2Exception

class ClientAlreadyExistsException(
    clientId: String,
): OAuth2Exception(
    oauth2Error = OAuth2Error("already exists", "client with clientId $clientId already exists", null),
    httpStatus = HttpStatus.BAD_REQUEST
)