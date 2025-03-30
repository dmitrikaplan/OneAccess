package ru.kaplaan.kcloak.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.jooq.tables.records.RoleRecord

@ConfigurationProperties(prefix = "realm")
data class RealmProperties(
    val name: String,
    val enabled: Boolean,
    val accessTokenLifespan: Int,
    val refreshTokenLifespan: Int,
    val users: Set<OneAccessUser> = setOf(),
    val roles: Set<OneAccessRole> = setOf(),
    val clients: Set<OneAccessClient> = setOf()
)

data class OneAccessUser(
    val username: String,
    val email: String,
    val enabled: Boolean,
    val firstName: String,
    val lastName: String,
    val password: String,
    val roles: Set<String> = setOf()
)

data class OneAccessRole(
    val name: String,
    val permissions: Set<String>
)

data class OneAccessClient(
    val clientId: String,
    val clientSecret: String,
    val enabled: Boolean,
    val clientScopes: Set<String>
)