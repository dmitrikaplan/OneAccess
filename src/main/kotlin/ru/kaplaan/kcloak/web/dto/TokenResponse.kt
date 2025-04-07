package ru.kaplaan.kcloak.web.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

data class TokenResponse(
    @JsonProperty("access_token")
    val accessTokenValue: String,
    @JsonProperty("refresh_token")
    val refreshTokenValue: String?,
    @JsonProperty("id_token_value")
    val idTokenValue: String?,
    //TODO: добавить остальные поля
)