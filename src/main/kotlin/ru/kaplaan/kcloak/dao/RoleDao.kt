package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.jooq.impl.DSL.trueCondition
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.domain.paging
import ru.kaplaan.kcloak.jooq.tables.records.RoleRecord
import ru.kaplaan.kcloak.jooq.tables.records.RoleUserRecord
import ru.kaplaan.kcloak.jooq.tables.references.ROLE
import ru.kaplaan.kcloak.jooq.tables.references.ROLE_USER

@Component
class RoleDao(
    private val db: DSLContext
) {

    fun saveRole(role: RoleRecord): RoleRecord? {
        return db.insertInto(ROLE)
            .set(role)
            .returningResult()
            .fetchOneInto(RoleRecord::class.java)
    }

    fun saveAllRoleUser(roleUsers: Set<RoleUserRecord>) {
        db.insertInto(ROLE_USER)
            .set(roleUsers)
            .onConflictDoNothing()
            .execute()
    }

    fun getRoleIdsByRoleNames(roleNames: Set<String>): List<Long> {
        return db.select(ROLE.ID)
            .from(ROLE)
            .where(ROLE.NAME.`in`(roleNames))
            .fetchInto(Long::class.java)
    }

    fun findRoleByName(roleName: String): RoleRecord? {
        return db.selectFrom(ROLE)
            .where(ROLE.NAME.eq(roleName))
            .fetchOne()
    }

    fun getAll(): List<RoleRecord> {
        return db.selectFrom(ROLE)
            .where(trueCondition())
            .fetchInto(RoleRecord::class.java)
    }

    fun findByUserId(userId: Long): List<RoleRecord> {
        return db.select(ROLE.NAME)
            .from(ROLE_USER)
            .join(ROLE).on(ROLE_USER.ROLE_ID.eq(ROLE.ID))
            .where(ROLE_USER.USER_ID.eq(userId))
            .fetchInto(RoleRecord::class.java)
    }

    fun findAll(pageNumber: Int, pageSize: Int): List<RoleRecord> {
        return db.select(ROLE.ID, ROLE.NAME)
            .from(ROLE)
            .where(trueCondition())
            .paging(pageNumber, pageSize)
            .fetchInto(RoleRecord::class.java)
    }
}