package ru.kaplaan.kcloak.dao

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.transaction.annotation.Transactional

open class CachedRegisteredClientRepository(
    jdbcOperations: JdbcOperations,
    maxCacheSize: Long,
) : RegisteredClientRepository {

    private val cache = Caffeine.newBuilder().maximumSize(maxCacheSize).build<String, RegisteredClient>()
    private val jdbcRegisteredClientRepository = JdbcRegisteredClientRepository(jdbcOperations)

    @Transactional
    override fun save(registeredClient: RegisteredClient) {
        jdbcRegisteredClientRepository.save(registeredClient)
        cache.put(registeredClient.clientId, registeredClient)
    }

    override fun findById(id: String): RegisteredClient? {
        return cache.asMap().toList().find { it.second.id == id }?.second ?: jdbcRegisteredClientRepository.findById(id)
    }

    override fun findByClientId(clientId: String): RegisteredClient? {
        return cache.get(clientId) {
            jdbcRegisteredClientRepository.findByClientId(clientId)
        }
    }
}