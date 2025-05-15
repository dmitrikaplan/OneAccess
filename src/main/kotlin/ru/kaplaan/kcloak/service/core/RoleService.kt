package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.dao.RoleDao
import ru.kaplaan.kcloak.domain.exception.role.RoleAlreadyExistsException
import ru.kaplaan.kcloak.domain.exception.role.RoleNotFoundException
import ru.kaplaan.kcloak.jooq.tables.records.RoleRecord
import ru.kaplaan.kcloak.jooq.tables.records.RoleUserRecord
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.mapper.toRecord
import ru.kaplaan.kcloak.web.mapper.toRoleDto

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
        val roleId = if (roleByName == null) {
            checkNotNull(roleDao.saveRole(roleRecord)?.id)
        } else checkNotNull(roleByName.id)

        permissionService.updateRolePermissions(role.permissions, roleId)
    }

    @Transactional
    fun updateRole(role: OneAccessRole, roleId: Long): RoleDto {
        findRoleByRoleName(role.name) ?: throw RoleNotFoundException(roleId)
        val roleRecord = roleDao.updateRole(role.toRecord(), roleId) ?: throw RoleNotFoundException(roleId)
        permissionService.updateRolePermissions(role.permissions, roleId)

        return roleRecord.toRoleDto(role.permissions)
    }

    @Transactional
    fun createRole(role: OneAccessRole): RoleDto {
        val roleRecord = role.toRecord()
        if (findRoleByRoleName(role.name) != null)
            throw RoleAlreadyExistsException(role.name)

        val savedRoleRecord = checkNotNull(roleDao.saveRole(roleRecord))

        return savedRoleRecord.toRoleDto(role.permissions)
    }

    @Transactional
    fun updateUserRoles(roles: Set<String>, userId: Long) {
        val newRolesIds = getRoleIdsByRoleNames(roles)
        val actualRolesIds = getRolesIdsByUserId(userId)
        val rolesToUserMapToDelete = actualRolesIds - newRolesIds
        deleteRolesFromUserByRolesIds(rolesToUserMapToDelete, userId)

        val rolesToUser = newRolesIds.map { roleId ->
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

    fun getRoleByRoleId(roleId: Long): RoleDto {
        val role = roleDao.findByRoleId(roleId) ?: throw RoleNotFoundException(roleId)
        val permissions = permissionService.getPermissionsNamesByRoleName(checkNotNull(role.name))
        return role.toRoleDto(permissions)
    }

    fun getByUserId(userId: Long): Set<String> {
        return roleDao.findByUserId(userId).mapNotNull { it.name }.toSet()
    }

    fun getRoleIdsByRoleNames(roles: Set<String>): Set<Long> {
        return roleDao.getRoleIdsByRoleNames(roles).toSet()
    }

    fun getRolesIdsByUserId(userId: Long): Set<Long> {
        return roleDao.getRolesIdsByUserId(userId).toSet()
    }

    fun deleteRolesFromUserByRolesIds(rolesIds: Set<Long>, userId: Long) {
        return roleDao.deleteRolesFromUserByRolesIds(rolesIds, userId)
    }

    @Transactional
    fun getAll(pageNumber: Int): List<RoleDto> {
        return roleDao.findAll(pageNumber, pageSize).map { role ->
            val permissions = permissionService.getPermissionsNamesByRoleName(checkNotNull(role.name))
            role.toRoleDto(permissions)
        }.distinct()
    }


}