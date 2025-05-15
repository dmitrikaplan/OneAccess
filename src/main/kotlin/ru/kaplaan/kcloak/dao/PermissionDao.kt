package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.jooq.tables.records.PermissionRecord
import ru.kaplaan.kcloak.jooq.tables.records.RolePermissionRecord
import ru.kaplaan.kcloak.jooq.tables.references.PERMISSION
import ru.kaplaan.kcloak.jooq.tables.references.ROLE
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


    fun getPermissionIdsByPermissionNames(permissions: Set<String>): List<Long> {
        return db.select(PERMISSION.ID)
            .from(PERMISSION)
            .where(PERMISSION.NAME.`in`(permissions))
            .fetchInto(Long::class.java)
    }


    fun saveAllRolePermission(rolePermissions: Set<RolePermissionRecord>): Int {
        return rolePermissions.map {
            db.insertInto(ROLE_PERMISSION)
                .set(it)
                .onConflictDoNothing()
                .execute()
        }.size
    }


    fun getByRoleName(roleName: String): MutableList<String> {
        return db.select(PERMISSION.NAME)
            .from(PERMISSION)
            .join(ROLE_PERMISSION).on(PERMISSION.ID.eq(ROLE_PERMISSION.PERMISSION_ID))
            .join(ROLE).on(ROLE_PERMISSION.ROLE_ID.eq(ROLE.ID))
            .where(ROLE.NAME.eq(roleName))
            .fetchInto(String::class.java)
    }

    fun deletePermissionsFromRole(permissionsIds: Set<Long>, roleId: Long) {
        db.deleteFrom(ROLE_PERMISSION)
            .where(ROLE_PERMISSION.PERMISSION_ID.`in`(permissionsIds).and(ROLE_PERMISSION.ROLE_ID.eq(roleId)))
            .execute()
    }

    fun getPermissionsIdsByRoleId(): List<Long> {
        return db.select(ROLE_PERMISSION.PERMISSION_ID)
            .from(ROLE_PERMISSION)
            .fetchInto(Long::class.java)
    }

}