package ru.kaplaan.kcloak.domain.exception

import org.springframework.http.ResponseEntity
import org.springframework.security.oauth2.core.OAuth2Error
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(OAuth2Exception::class)
    fun handleOAuth2Exception(e: OAuth2Exception): ResponseEntity<OAuth2Error> {
        return ResponseEntity.status(e.httpStatus).body(e.oauth2Error)
    }
}