package ru.kaplaan.kcloak.web.mapper

import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.web.dto.User
import ru.kaplaan.kcloak.jooq.tables.records.UsersRecord
import ru.kaplaan.kcloak.web.dto.UserDto

fun OneAccessUser.toRecord(): UsersRecord {
    return UsersRecord().apply {
        this.username = this@toRecord.username
        this.email = this@toRecord.email
        this.enabled = this@toRecord.enabled
        this.firstName = this@toRecord.firstName
        this.lastName = this@toRecord.lastName
        this.password = this@toRecord.password
    }
}

fun UsersRecord.toOneAccessUser(roles: Set<String>): OneAccessUser {
    return OneAccessUser(
        username = checkNotNull(this.username),
        email = checkNotNull(this.email),
        enabled = checkNotNull(this.enabled),
        firstName = checkNotNull(this.firstName),
        lastName = checkNotNull(this.lastName),
        password = checkNotNull(this.password),
        roles = roles
    )
}

fun UsersRecord.toUser(permissions: Set<String>, roles: Set<String>): User {
    return User(
        id = checkNotNull(this.id),
        username = checkNotNull(this.username),
        email = checkNotNull(this.email),
        enabled = checkNotNull(this.enabled),
        firstName = checkNotNull(this.firstName),
        lastName = checkNotNull(this.lastName),
        password = checkNotNull(this.password),
        permissions = permissions,
        roles = roles
    )
}

fun UsersRecord.toUserDto(roles: Set<String>): UserDto {
    return UserDto(
        id = checkNotNull(this.id),
        username = checkNotNull(this.username),
        email = checkNotNull(this.email),
        enabled = checkNotNull(this.enabled),
        firstName = checkNotNull(this.firstName),
        lastName = checkNotNull(this.lastName),
        roles = roles
    )
}