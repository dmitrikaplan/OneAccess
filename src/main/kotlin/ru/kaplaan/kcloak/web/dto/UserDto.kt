package ru.kaplaan.kcloak.web.dto

data class UserDto(
    val id: Long,
    val username: String,
    val email: String,
    val enabled: Boolean,
    val firstName: String,
    val lastName: String,
    val roles: Set<String>
)