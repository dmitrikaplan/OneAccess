package ru.kaplaan.kcloak.web.mapper

import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.jooq.tables.records.RoleRecord
import ru.kaplaan.kcloak.web.dto.RoleDto

fun OneAccessRole.toRecord(): RoleRecord {
    return RoleRecord().apply {
        this.name = this@toRecord.name
    }
}

fun RoleRecord.toOneAccessRole(permissions: Set<String>): OneAccessRole {
    return OneAccessRole(
        name = checkNotNull(this.name),
        permissions = permissions
    )
}

fun RoleRecord.toRoleDto(permissions: Set<String>): RoleDto {
    return RoleDto(
        roleId = checkNotNull(this.id),
        name = checkNotNull(this.name),
        permissions = permissions
    )
}