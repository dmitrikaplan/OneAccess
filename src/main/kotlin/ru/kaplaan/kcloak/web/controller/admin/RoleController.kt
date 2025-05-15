package ru.kaplaan.kcloak.web.controller.admin

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.service.core.RoleService
import ru.kaplaan.kcloak.web.dto.RoleDto

@RestController
@RequestMapping("/admin/roles")
class RoleController(
    private val roleService: RoleService,
) {

    @GetMapping("/all/{pageNumber}")
    @PreAuthorize("hasAuthority('READ_ROLES')")
    fun getAllRoles(@PathVariable pageNumber: Int): List<RoleDto> {
        return roleService.getAll(pageNumber)
    }

    @GetMapping("/{roleId}")
    @PreAuthorize("hasAuthority('READ_ROLES')")
    fun getRoleById(@PathVariable roleId: Long): RoleDto {
        return roleService.getRoleByRoleId(roleId)
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_ROLES')")
    fun createRole(@Validated @RequestBody role: OneAccessRole): RoleDto {
        return roleService.createRole(role)
    }

    @PutMapping("/{roleId}")
    @PreAuthorize("hasAuthority('WRITE_ROLES')")
    fun updateRole(@Validated @RequestBody role: OneAccessRole, @PathVariable roleId: Long): RoleDto {
        return roleService.updateRole(role, roleId)
    }
}