package ru.kaplaan.kcloak.service.core

import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.dao.ClientDao
import ru.kaplaan.kcloak.domain.exception.client.ClientAlreadyExistsException
import ru.kaplaan.kcloak.domain.exception.client.ClientNotFoundException
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.mapper.toClient
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

    @Transactional
    fun create(client: OneAccessClient): ClientDto {
        if(registeredClientRepository.findByClientId(client.clientId) != null)
            throw ClientAlreadyExistsException(client.clientId)

        val registeredClient = client.toRegisteredClient()
        registeredClientRepository.save(registeredClient)

        return registeredClient.toClient()
    }

    @Transactional
    fun update(client: OneAccessClient, clientId: String): ClientDto {
        if(registeredClientRepository.findByClientId(clientId) == null)
            throw ClientNotFoundException(client.clientId)

        val registeredClient = client.toRegisteredClient()
        registeredClientRepository.save(registeredClient)

        return registeredClient.toClient()
    }


    fun getClientByClientId(clientId: String): ClientDto {
        return registeredClientRepository.findByClientId(clientId)?.toClient()
            ?: throw ClientNotFoundException(clientId)
    }

    fun getAll(pageNumber: Int): List<ClientDto> {
        return clientDao.getAllClientIds(pageNumber, pageSize).mapNotNull { clientId ->
            registeredClientRepository.findByClientId(clientId)
        }.map {
            it.toClient()
        }
    }
}