package ru.kaplaan.kcloak.web.controller.admin

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.service.core.RoleService

@RestController
@RequestMapping("/admin/roles")
class RoleController(
    private val roleService: RoleService
) {

    @GetMapping("/all/{pageNumber}")
    fun getAllRoles(@PathVariable pageNumber: Int): List<OneAccessRole> {
        return roleService.findAll(pageNumber)
    }
}