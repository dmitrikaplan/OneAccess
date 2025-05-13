package ru.kaplaan.kcloak.domain.exception.role

import org.springframework.http.HttpStatus
import org.springframework.security.oauth2.core.OAuth2Error
import ru.kaplaan.kcloak.domain.exception.OAuth2Exception

class RoleNotFoundException(
    roleId: Long
): OAuth2Exception(
    oauth2Error = OAuth2Error("not found", "role with id $roleId not found", null),
    httpStatus = HttpStatus.BAD_REQUEST
)