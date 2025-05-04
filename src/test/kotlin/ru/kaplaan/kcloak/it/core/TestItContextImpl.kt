package ru.kaplaan.kcloak.it.core

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.it.steps.AdminSteps

@Component
class TestItContextImpl: TestItContext {

    @Autowired
    lateinit var adminSteps: AdminSteps

    override fun getAllUsers(): Collection<OneAccessUser> {
        return adminSteps.findAllUsers()
    }

    override fun getAllClients(): Collection<OneAccessClient> {
        return adminSteps.getAllClients()
    }

    override fun getAllRoles(): Collection<OneAccessRole> {
        return adminSteps.getAllRoles()
    }
}