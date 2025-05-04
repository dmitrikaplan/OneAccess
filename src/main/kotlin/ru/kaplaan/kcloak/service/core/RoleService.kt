package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.dao.RoleDao
import ru.kaplaan.kcloak.jooq.tables.records.RoleRecord
import ru.kaplaan.kcloak.jooq.tables.records.RoleUserRecord
import ru.kaplaan.kcloak.web.mapper.toOneAccessRole
import ru.kaplaan.kcloak.web.mapper.toRecord

private const val pageSize = 10

@Service
class RoleService(
    private val roleDao: RoleDao,
    private val permissionService: PermissionService,
) {

    @Transactional
    fun saveRole(role: OneAccessRole) {
        val roleRecord = role.toRecord()
        val roleByName = findRoleByRoleName(role.name)
        val roleId = if(roleByName == null) {
            checkNotNull(roleDao.saveRole(roleRecord)?.id)
        } else checkNotNull(roleByName.id)

        permissionService.bindPermissionsToRole(role.permissions, roleId)
    }

    @Transactional
    fun bindRolesToUser(roles: Set<String>, userId: Long) {
        val rolesIds = roleDao.getRoleIdsByRoleNames(roles)
        val rolesToUser = rolesIds.map { roleId ->
            RoleUserRecord().apply {
                this.roleId = roleId
                this.userId = userId
            }
        }.toSet()

        roleDao.saveAllRoleUser(rolesToUser)
    }

    fun findRoleByRoleName(roleName: String): RoleRecord? {
        return roleDao.findRoleByName(roleName)
    }

    fun findByUserId(userId: Long): Set<String> {
        return roleDao.findByUserId(userId).mapNotNull { it.name }.toSet()
    }

    fun findAll(pageNumber: Int): List<OneAccessRole> {
        return roleDao.findAll(pageNumber, pageSize).map { role ->
            val permissions = permissionService.getPermissionsByRoleName(checkNotNull(role.name))
            role.toOneAccessRole(permissions)
        }.distinct()
    }

}