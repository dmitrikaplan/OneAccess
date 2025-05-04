package ru.kaplaan.kcloak.domain.exception.client

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error
import ru.kaplaan.kcloak.domain.exception.OAuth2Exception

class ClientNotFoundException(
    clientId: String,
) : OAuth2Exception(
    oauth2Error = OAuth2Error("client_not_found", "client with client_id $clientId not found", null),
    httpStatus = HttpStatus.BAD_REQUEST,
)