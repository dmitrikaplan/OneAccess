package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.dao.PermissionDao
import ru.kaplaan.kcloak.jooq.tables.records.RolePermissionRecord

@Service
class PermissionService(
    private val permissionDao: PermissionDao,
) {

    @Transactional
    fun updateRolePermissions(permissions: Set<String>, roleId: Long) {
        val newPermissionsIds = permissionDao.getPermissionIdsByPermissionNames(permissions).toSet()
        val actualPermissionsIds = getPermissionsIdsByRoleId(roleId).toSet()
        val permissionsToRoleMapToDelete = newPermissionsIds - actualPermissionsIds
        deletePermissionsFromRoleByPermissionIds(permissionsToRoleMapToDelete, roleId)

        val rolesToPermission = newPermissionsIds.map { permissionId ->
            RolePermissionRecord().apply {
                this.roleId = roleId
                this.permissionId = permissionId
            }
        }.toSet()

        permissionDao.saveAllRolePermission(rolesToPermission)
    }

    fun getPermissionsIdsByRoleId(roleId: Long): List<Long> {
        return permissionDao.getPermissionsIdsByRoleId()
    }

    fun getPermissionsNamesByRoleName(roleName: String): Set<String> {
        return permissionDao.getByRoleName(roleName).toSet()
    }

    private fun deletePermissionsFromRoleByPermissionIds(permissionsIds: Set<Long>, roleId: Long) {
        permissionDao.deletePermissionsFromRole(permissionsIds, roleId)
    }
}