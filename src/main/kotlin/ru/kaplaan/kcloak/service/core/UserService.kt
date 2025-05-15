package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.dao.UserDao
import ru.kaplaan.kcloak.domain.exception.user.UserAlreadyExistsException
import ru.kaplaan.kcloak.domain.exception.user.UserNotFoundException
import ru.kaplaan.kcloak.jooq.tables.records.UsersRecord
import ru.kaplaan.kcloak.web.dto.User
import ru.kaplaan.kcloak.web.dto.UserDto
import ru.kaplaan.kcloak.web.mapper.toRecord
import ru.kaplaan.kcloak.web.mapper.toUser
import ru.kaplaan.kcloak.web.mapper.toUserDto

private const val pageSize: Int = 10

@Service
class UserService(
    private val userDao: UserDao,
    private val roleService: RoleService,
    private val permissionService: PermissionService,
) {

    fun findByEmail(email: String): User? {
        val user = userDao.findByEmail(email) ?: return null
        return fetchAuthoritiesAndMapToUser(user)
    }

    @Transactional
    fun save(oneAccessUser: OneAccessUser) {
        val userByEmail = findByEmail(oneAccessUser.email)
        if (userByEmail != null) {
            update(oneAccessUser, checkNotNull(userByEmail.id))
        } else {
            create(oneAccessUser)
        }
    }

    @Transactional
    fun create(oneAccessUser: OneAccessUser): UserDto {
        if (findByEmail(oneAccessUser.email) != null)
            throw UserAlreadyExistsException(oneAccessUser.email)

        val userRecord = oneAccessUser.toRecord()
        val savedUser = checkNotNull(userDao.create(userRecord))
        roleService.updateUserRoles(oneAccessUser.roles, checkNotNull(savedUser.id))

        return fetchAuthoritiesAndMapToUserDto(savedUser)
    }

    fun update(user: OneAccessUser, userId: Long): UserDto {
        val userRecord = user.toRecord()
        val savedUser = checkNotNull(userDao.update(userRecord, userId))
        roleService.updateUserRoles(user.roles, userId)
        return fetchAuthoritiesAndMapToUserDto(savedUser)
    }

    fun getByUserId(userId: Long): UserDto {
        val userRecord = userDao.findByUserId(userId) ?: throw UserNotFoundException(userId)
        return fetchAuthoritiesAndMapToUserDto(userRecord)
    }

    fun getAll(pageNumber: Int): List<UserDto> {
        return userDao.getAll(pageNumber, pageSize).map { user ->
            fetchAuthoritiesAndMapToUserDto(user)
        }
    }

    private fun fetchAuthoritiesAndMapToUser(user: UsersRecord): User {
        val roles = roleService.getByUserId(checkNotNull(user.id))
        val permissions = roles.flatMap {
            permissionService.getPermissionsNamesByRoleName(it)
        }.distinct()

        return user.toUser(permissions.toSet(), roles)
    }

    private fun fetchAuthoritiesAndMapToUserDto(user: UsersRecord): UserDto {
        val roles = roleService.getByUserId(checkNotNull(user.id))
        return user.toUserDto(roles)
    }

}