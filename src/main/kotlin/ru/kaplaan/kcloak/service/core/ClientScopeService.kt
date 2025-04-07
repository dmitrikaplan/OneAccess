package ru.kaplaan.kcloak.service.core

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import ru.kaplaan.kcloak.config.properties.SupportedScopes
import ru.kaplaan.kcloak.dao.ClientScopeDao
import ru.kaplaan.kcloak.jooq.tables.records.ClientClientScopeRecord

@Service
class ClientScopeService(
    private val clientScopeDao: ClientScopeDao,
) {

    private val log = LoggerFactory.getLogger(ClientScopeService::class.java)

    fun bindScopesToClient(clientScopes: Set<String>, clientId: Long) {
        val supportedScopes = SupportedScopes.getSupportedScopes()
        clientScopes.mapNotNull {
            val clientScope = supportedScopes.find { supportedScope ->
                supportedScope == it.lowercase()
            }
            if(clientScope == null) {
                log.warn("client scope with name $it not supported!")
            }
            clientScope
        }.map {
            ClientClientScopeRecord().apply {
                this.clientId = clientId
                this.clientScope = it
            }
        }.forEach {
            clientScopeDao.save(it)
        }
    }
}