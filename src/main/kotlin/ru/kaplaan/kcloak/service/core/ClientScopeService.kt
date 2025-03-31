package ru.kaplaan.kcloak.service.core

import org.springframework.stereotype.Service
import ru.kaplaan.kcloak.config.properties.SupportedScopes

@Service
class ClientScopeService {

    fun bindScopesToClient(clientScopes: Set<SupportedScopes>, clientId: Long) {
        clientScopes.map { it.value }
        //TODO: добавить bind scopов
    }
}