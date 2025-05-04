package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.dao.PermissionDao
import ru.kaplaan.kcloak.jooq.tables.records.RolePermissionRecord

@Service
class PermissionService(
    private val permissionDao: PermissionDao
) {

    @Transactional
    fun bindPermissionsToRole(permissions: Set<String>, roleId: Long) {
        val permissionsIds = permissionDao.getPermissionIdsByPermissionNames(permissions)
        val rolesToPermission = permissionsIds.map { permissionId ->
            RolePermissionRecord().apply {
                this.roleId = roleId
                this.permissionId = permissionId
            }
        }.toSet()

        permissionDao.saveAllRolePermission(rolesToPermission)
    }

    fun getPermissionsByRoleName(roleName: String): Set<String> {
        return permissionDao.getByRoleName(roleName).toSet()
    }
}