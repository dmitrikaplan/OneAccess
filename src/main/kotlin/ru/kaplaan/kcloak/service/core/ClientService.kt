package ru.kaplaan.kcloak.service.core

import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.dao.ClientDao
import ru.kaplaan.kcloak.domain.exception.client.ClientNotFoundException
import ru.kaplaan.kcloak.web.mapper.toOneAccessClient
import ru.kaplaan.kcloak.web.mapper.toRegisteredClient

private const val pageSize = 10

@Service
class ClientService(
    private val clientDao: ClientDao,
    private val registeredClientRepository: RegisteredClientRepository,
) {

    @Transactional
    fun save(client: OneAccessClient) {
        registeredClientRepository.save(client.toRegisteredClient())
    }

    fun getClientByClientId(clientId: String): OneAccessClient {
        return registeredClientRepository.findByClientId(clientId)?.toOneAccessClient()
            ?: throw ClientNotFoundException(clientId)
    }

    fun getAll(pageNumber: Int): List<OneAccessClient> {
        return clientDao.getAllClientIds(pageNumber, pageSize).mapNotNull { clientId ->
            registeredClientRepository.findByClientId(clientId)
        }.map {
            it.toOneAccessClient()
        }
    }
}