package ru.kaplaan.kcloak

import org.junit.jupiter.api.Test
import org.springframework.test.context.ActiveProfiles
import ru.kaplaan.kcloak.config.properties.OneAccessProperties
import ru.kaplaan.kcloak.dao.*
import ru.kaplaan.kcloak.service.core.*

@ActiveProfiles("test", "test-realm")
class RealmConfigurationIT(
    private val permissionDao: PermissionDao,
    private val roleDao: RoleDao,
    private val userDao: UserDao,
    private val clientDao: ClientDao,
    private val clientScopeDao: ClientScopeDao,
    private val oneAccessProperties: OneAccessProperties,
) {


    @Test
    fun checkConfiguration() {
        val permissions = permissionDao.getAll().map { it.name!! }
        val roles = roleDao.getAll().map { it.name!! }
        val users = userDao.getAll()
        val clients = clientDao.getAll()

        val realms = oneAccessProperties.realms
        val expectedUsers = realms.map { it.value.users }.flatten()
    }
}