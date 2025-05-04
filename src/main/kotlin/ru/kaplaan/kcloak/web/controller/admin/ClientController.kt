package ru.kaplaan.kcloak.web.controller.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.service.core.ClientService

@RestController
@RequestMapping("/admin/clients")
class ClientController(
    private val clientService: ClientService
) {

    @GetMapping("/{clientId}")
    fun getClientById(@PathVariable clientId: String): OneAccessClient {
        return clientService.getClientByClientId(clientId)
    }

    @GetMapping("/all/{pageNumber}")
    fun getAllClients(@PathVariable pageNumber: Int): List<OneAccessClient> {
        return clientService.getAll(pageNumber)
    }
}