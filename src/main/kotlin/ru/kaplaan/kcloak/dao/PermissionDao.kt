package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.jooq.tables.records.PermissionRecord
import ru.kaplaan.kcloak.jooq.tables.records.RolePermissionRecord
import ru.kaplaan.kcloak.jooq.tables.references.PERMISSION
import ru.kaplaan.kcloak.jooq.tables.references.ROLE_PERMISSION

@Component
class PermissionDao(
    private val db: DSLContext
) {

    fun save(permission: PermissionRecord) {
        db.insertInto(PERMISSION)
            .set(permission)
            .onConflictDoNothing()
            .execute()
    }


    fun getPermissionIdsByPermissionNames(permissions: Set<String>) : List<Long> {
        return db.select(PERMISSION.ID)
            .from(PERMISSION)
            .where(PERMISSION.NAME.`in`(permissions))
            .fetchInto(Long::class.java)
    }


    fun saveAllRolePermission(rolePermissions: Set<RolePermissionRecord>) {
        db.insertInto(ROLE_PERMISSION)
            .set(rolePermissions)
            .onConflictDoNothing()
            .execute()
    }

}