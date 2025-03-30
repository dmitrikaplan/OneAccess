package ru.kaplaan.kcloak.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.dao.UserDao
import ru.kaplaan.kcloak.domain.exception.UserNotFoundException
import ru.kaplaan.kcloak.web.mapper.toRecord

@Service
class UserService(
    private val userDao: UserDao,
    private val roleService: RoleService
) {

    fun existsByUsernameOrEmail(username: String, email: String): Boolean {
        return userDao.findByUsername(username) != null && userDao.findByEmail(email) != null
    }

    fun getUserIdByUsername(username: String): Long {
        return userDao.findByUsername(username)?.id
            ?: throw UserNotFoundException("user not found by username")
    }

    @Transactional
    fun save(user: OneAccessUser){
        if(existsByUsernameOrEmail(user.username, user.email)){
            update(user)
        } else {
            create(user)
        }
    }

    private fun create(user: OneAccessUser){
        val userRecord = user.toRecord()
        val userId = checkNotNull(userDao.create(userRecord)?.id)
        roleService.bindRolesToUser(user.roles, userId)
    }

    private fun update(user: OneAccessUser){
        val userRecord = user.toRecord()
        val userId = getUserIdByUsername(user.username)
        userDao.update(userRecord, userId)
        roleService.bindRolesToUser(user.roles, userId)
    }
}