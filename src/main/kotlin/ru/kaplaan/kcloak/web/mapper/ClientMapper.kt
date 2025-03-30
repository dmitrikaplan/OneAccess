package ru.kaplaan.kcloak.web.mapper

import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.jooq.tables.records.ClientRecord

fun OneAccessClient.toRecord(): ClientRecord {
    return ClientRecord().apply {
        this.clientId = this@toRecord.clientId
        this.clientSecret = this@toRecord.clientSecret
        this.enabled = this@toRecord.enabled
    }
}