package ru.kaplaan.kcloak.web.dto

data class RoleDto(
    val roleId: Long,
    val name: String,
    val permissions: Set<String>
)