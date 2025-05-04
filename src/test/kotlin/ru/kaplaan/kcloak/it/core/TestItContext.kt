package ru.kaplaan.kcloak.it.core

import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.config.properties.OneAccessUser


interface TestItContext {

    fun getAllUsers(): Collection<OneAccessUser>

    fun getAllClients(): Collection<OneAccessClient>

    fun getAllRoles(): Collection<OneAccessRole>

}
