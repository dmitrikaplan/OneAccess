package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.dao.UserDao
import ru.kaplaan.kcloak.jooq.tables.records.UsersRecord
import ru.kaplaan.kcloak.web.mapper.toRecord

@Service
class UserService(
    private val userDao: UserDao,
    private val roleService: RoleService
) {

    fun findByEmail(email: String): UsersRecord? {
        return userDao.findByEmail(email)
    }

    @Transactional
    fun save(user: OneAccessUser){
        val userByUsername = findByEmail(user.email)
        if (userByUsername != null) {
            update(user, checkNotNull(userByUsername.id))
        } else {
            create(user)
        }
    }

    private fun create(user: OneAccessUser){
        val userRecord = user.toRecord()
        val userId = checkNotNull(userDao.create(userRecord)?.id)
        roleService.bindRolesToUser(user.roles, userId)
    }

    private fun update(user: OneAccessUser, userId: Long) {
        val userRecord = user.toRecord()
        userDao.update(userRecord, userId)
        roleService.bindRolesToUser(user.roles, userId)
    }
}