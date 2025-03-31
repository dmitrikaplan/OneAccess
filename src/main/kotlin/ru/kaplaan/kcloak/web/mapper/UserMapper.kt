package ru.kaplaan.kcloak.web.mapper

import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.jooq.tables.records.UsersRecord

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