package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.dao.ClientDao
import ru.kaplaan.kcloak.jooq.tables.records.ClientRecord
import ru.kaplaan.kcloak.web.mapper.toRecord

@Service
class ClientService(
    private val clientDao: ClientDao,
    private val clientScopeService: ClientScopeService
) {

    @Transactional
    fun save(client: OneAccessClient) {
        val clientByClientId = findClientByClientId(client.clientId)
        if (clientByClientId == null)
            create(client)
        else
            update(client, checkNotNull(clientByClientId.id))
    }

    fun create(client: OneAccessClient) {
        val clientRecord = client.toRecord()
        val clientId = checkNotNull(clientDao.create(clientRecord)?.id)
        clientScopeService.bindScopesToClient(client.clientScopes, clientId)
    }

    fun update(client: OneAccessClient, clientId: Long) {
        val clientRecord = client.toRecord()
        clientDao.update(clientRecord, clientId)
        clientScopeService.bindScopesToClient(client.clientScopes, clientId)
    }

    fun findClientByClientId(clientId: String): ClientRecord? {
        return clientDao.findByClientId(clientId)
    }
}