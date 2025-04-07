package ru.kaplaan.kcloak.config.properties

import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.service.core.PermissionService
import ru.kaplaan.kcloak.service.core.RoleService
import ru.kaplaan.kcloak.service.core.UserService
import ru.kaplaan.kcloak.service.core.ClientService

@Component
class RealmPropertiesValidator(
    private val oneAccessProperties: OneAccessProperties,
    private val userService: UserService,
    private val roleService: RoleService,
    private val permissionService: PermissionService,
    private val clientService: ClientService
) {

    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun initRealmProperties() {
        validateRealmProperties()
    }

    fun validateRealmProperties() {
        oneAccessProperties.realms.forEach{ (realmName, realm) ->

            for(role in realm.roles) {
                roleService.saveRole(role)
            }

            for (user in realm.users) {
                userService.save(user)
            }

            for(client in realm.clients) {
                clientService.save(client)
            }
        }

    }
}