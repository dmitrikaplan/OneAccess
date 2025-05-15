package ru.kaplaan.kcloak.web.controller.admin

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.service.core.ClientService
import ru.kaplaan.kcloak.web.dto.ClientDto

@RestController
@RequestMapping("/admin/clients")
class ClientController(
    private val clientService: ClientService,
) {

    @GetMapping("/{clientId}")
    @PreAuthorize("hasAuthority('READ_CLIENTS')")
    fun getClientById(@PathVariable clientId: String): ClientDto {
        return clientService.getClientByClientId(clientId)
    }

    @GetMapping("/all/{pageNumber}")
    @PreAuthorize("hasAuthority('READ_CLIENTS')")
    fun getAllClients(@PathVariable pageNumber: Int): List<ClientDto> {
        return clientService.getAll(pageNumber)
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_CLIENTS')")
    fun createClient(@RequestBody @Validated client: OneAccessClient): ClientDto {
        return clientService.create(client)
    }

    @PutMapping("/{clientId}")
    @PreAuthorize("hasAuthority('WRITE_CLIENTS')")
    fun updateClient(@RequestBody @Validated client: OneAccessClient, @PathVariable clientId: String): ClientDto {
        return clientService.update(client, clientId)
    }
}