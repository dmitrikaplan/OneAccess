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

    fun findByUsername(username: String): UsersRecord? {
        return db.selectFrom(USERS)
            .where(USERS.USERNAME.eq(username))
            .fetchOne()
    }

    fun create(user: UsersRecord): UsersRecord? {
        return db.insertInto(USERS)
            .set(user)
            .returningResult()
            .fetchOneInto(UsersRecord::class.java)
    }


    fun update(user: UsersRecord, userId: Long): UsersRecord? {
        return db.update(USERS)
            .set(user)
            .where(USERS.ID.eq(userId))
            .returning()
            .fetchOneInto(UsersRecord::class.java)
    }

    fun findByUserId(userId: Long): UsersRecord? {
        return db.selectFrom(USERS)
            .where(USERS.ID.eq(userId))
            .fetchOneInto(UsersRecord::class.java)
    }

    fun getAll(pageNumber: Int, pageSize: Int): List<UsersRecord> {
        val from = (pageNumber - 1) * pageSize
        return db.selectFrom(USERS)
            .offset(from)
            .limit(pageSize)
            .fetchInto(UsersRecord::class.java)
    }
}