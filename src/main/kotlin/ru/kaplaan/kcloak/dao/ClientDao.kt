package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.jooq.tables.records.ClientRecord
import ru.kaplaan.kcloak.jooq.tables.references.CLIENT

@Component
class ClientDao(
    private val db: DSLContext
) {


    fun findByClientId(clientId: String): ClientRecord? {
        return db.selectFrom(CLIENT)
            .where(CLIENT.CLIENT_ID.eq(clientId))
            .fetchOne()
    }

}