package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.jooq.tables.records.ClientRecord
import ru.kaplaan.kcloak.jooq.tables.records.PermissionRecord
import ru.kaplaan.kcloak.jooq.tables.references.CLIENT
import ru.kaplaan.kcloak.jooq.tables.references.PERMISSION
import ru.kaplaan.kcloak.jooq.tables.references.USERS

@Component
class ClientDao(
    private val db: DSLContext
) {


    fun findByClientId(clientId: String): ClientRecord? {
        return db.selectFrom(CLIENT)
            .where(CLIENT.CLIENT_ID.eq(clientId))
            .fetchOne()
    }

    fun create(clientRecord: ClientRecord): ClientRecord? {
        return db.insertInto(CLIENT)
            .set(clientRecord)
            .returningResult()
            .fetchOneInto(ClientRecord::class.java)
    }

    fun update(clientRecord: ClientRecord, clientId: Long) {
        db.update(CLIENT)
            .set(clientRecord)
            .where(USERS.ID.eq(clientId))
            .execute()
    }

    fun getAll(): List<ClientRecord> {
        return db.selectFrom(CLIENT)
            .where(trueCondition())
            .fetchInto(ClientRecord::class.java)
    }

}