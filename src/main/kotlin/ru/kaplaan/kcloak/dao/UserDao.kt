package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.jooq.tables.records.UsersRecord
import ru.kaplaan.kcloak.jooq.tables.references.USERS

@Component
class UserDao(
    private val db: DSLContext
) {

    fun findByEmail(email: String): UsersRecord? {
        return db.selectFrom(USERS)
            .where(USERS.EMAIL.eq(email))
            .fetchOne()
    }

    fun create(user: UsersRecord): UsersRecord? {
        return db.insertInto(USERS)
            .set(user)
            .returningResult()
            .fetchOneInto(UsersRecord::class.java)
    }


    fun update(user: UsersRecord, userId: Long){
        db.update(USERS)
            .set(user)
            .where(USERS.ID.eq(userId))
            .execute()
    }
}