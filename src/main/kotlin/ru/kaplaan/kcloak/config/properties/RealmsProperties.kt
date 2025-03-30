package ru.kaplaan.kcloak.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("realms")
data class RealmsProperties(
    val records: Set<RealmConfiguration> = setOf()
)

data class RealmConfiguration(
    val realm: String,
    val enabled: Boolean,
    val accessTokenLifespan: Int,
    val refreshTokenLifespan: Int,
    val users: Set<OneAccessUser>,
    val groups: Set<OneAccessGroup>,
    val roles: Set<OneAccessRole>,
    val clients: Set<OneAccessClient>
)

data class OneAccessUser(
    val username: String,
    val email: String,
    val enabled: Boolean,
    val firstName: String,
    val lastName: String,
    val password: String,
    val groups: Set<String> = setOf()
)

data class OneAccessGroup(
    val name: String,
    val realmRoles: Set<String>
)

data class OneAccessRole(
    val name: String,
)

data class OneAccessClient(
    val clientId: String,
    val enabled: Boolean,
    val clientScopes: Set<String>
)