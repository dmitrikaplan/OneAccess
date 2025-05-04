package ru.kaplaan.kcloak.config.properties

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.service.core.ClientService
import ru.kaplaan.kcloak.service.core.RoleService
import ru.kaplaan.kcloak.service.core.UserService

@Component
class RealmPropertiesValidator(
    private val oneAccessProperties: OneAccessProperties,
    private val userService: UserService,
    private val roleService: RoleService,
    private val clientService: ClientService
) {

    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun initRealmProperties() {
        validateRealmProperties()
    }

    fun validateRealmProperties() {


        for (role in oneAccessProperties.roles) {
            roleService.saveRole(role)
        }

        for (user in oneAccessProperties.users) {
            userService.save(user)
        }

        for (client in oneAccessProperties.clients) {
            clientService.save(client)
        }
    }

}