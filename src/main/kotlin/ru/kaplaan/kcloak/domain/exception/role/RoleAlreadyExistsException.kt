package ru.kaplaan.kcloak.domain.exception.role

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error
import ru.kaplaan.kcloak.domain.exception.OAuth2Exception

class RoleAlreadyExistsException(
    roleName: String
): OAuth2Exception(
    oauth2Error = OAuth2Error("already exists", "role with name $roleName already exists", null),
    httpStatus = HttpStatus.BAD_REQUEST
)