package ru.kaplaan.kcloak.domain.exception

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error

class RealmNotFoundException(
    realm: String
) : OAuth2Exception(
    oauth2Error = OAuth2Error("not found", "realm $realm is not found", null),
    httpStatus = HttpStatus.BAD_REQUEST
)