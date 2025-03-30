package ru.kaplaan.kcloak.web.mapper

import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.jooq.tables.records.RoleRecord

fun OneAccessRole.toRecord(): RoleRecord {
    return RoleRecord().apply {
        this.name = this@toRecord.name
    }
}