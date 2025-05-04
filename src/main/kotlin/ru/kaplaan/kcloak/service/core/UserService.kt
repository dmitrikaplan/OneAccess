package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.dao.UserDao
import ru.kaplaan.kcloak.domain.exception.UserNotFoundException
import ru.kaplaan.kcloak.jooq.tables.records.UsersRecord
import ru.kaplaan.kcloak.web.mapper.toOneAccessUser
import ru.kaplaan.kcloak.web.mapper.toRecord

private const val pageSize: Int = 10

@Service
class UserService(
    private val userDao: UserDao,
    private val roleService: RoleService
) {

    fun findByEmailAndRealm(email: String): UsersRecord? {
        return userDao.findByEmail(email)
    }

    @Transactional
    fun save(user: OneAccessUser) {
        val userByEmail = findByEmailAndRealm(user.email)
        if (userByEmail != null) {
            update(user, checkNotNull(userByEmail.id))
        } else {
            create(user)
        }
    }

    private fun create(user: OneAccessUser) {
        val userRecord = user.toRecord()
        val userId = checkNotNull(userDao.create(userRecord)?.id)
        roleService.bindRolesToUser(user.roles, userId)
    }

    private fun update(user: OneAccessUser, userId: Long) {
        val userRecord = user.toRecord()
        userDao.update(userRecord, userId)
        roleService.bindRolesToUser(user.roles, userId)
    }

    fun getByUserId(userId: Long): OneAccessUser {
        val userRecord = userDao.findByUserId(userId) ?: throw UserNotFoundException(userId)
        val userRoles = roleService.findByUserId(userId)
        return userRecord.toOneAccessUser(userRoles)
    }

    fun getAll(pageNumber: Int): List<OneAccessUser> {
        return userDao.getAll(pageNumber, pageSize).map { user ->
            val roles = roleService.findByUserId(checkNotNull(user.id))
            user.toOneAccessUser(roles)
        }
    }
}