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
    private val realmProperties: RealmProperties,
    private val userService: UserService,
    private val roleService: RoleService,
    private val permissionService: PermissionService,
    private val clientService: ClientService
) {

    @EventListener(ApplicationReadyEvent::class)
    @Transactional
    fun initRealmProperties() {
        //validateRealmProperties()
    }

    fun validateRealmProperties() {

        val permissions = realmProperties.roles.map { it.permissions }.flatten()
        for (permission in permissions) {
            permissionService.save(permission)
        }

        for(role in realmProperties.roles) {
            roleService.saveRole(role)
        }

        for (user in realmProperties.users) {
            userService.save(user)
        }

        for(client in realmProperties.clients) {
            clientService.save(client)
        }
    }
}