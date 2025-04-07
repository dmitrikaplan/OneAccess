package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.jooq.tables.records.ClientClientScopeRecord
import ru.kaplaan.kcloak.jooq.tables.records.PermissionRecord
import ru.kaplaan.kcloak.jooq.tables.references.CLIENT_CLIENT_SCOPE
import ru.kaplaan.kcloak.jooq.tables.references.PERMISSION

@Component
class ClientScopeDao(
    private val db: DSLContext
) {

    fun save(clientClientScopeRecord: ClientClientScopeRecord) {
        db.insertInto(CLIENT_CLIENT_SCOPE)
            .set(clientClientScopeRecord)
            .onConflictDoNothing()
            .execute()
    }


    fun getAll(): List<ClientClientScopeRecord> {
        return db.selectFrom(CLIENT_CLIENT_SCOPE)
            .where(trueCondition())
            .fetchInto(ClientClientScopeRecord::class.java)
    }

}