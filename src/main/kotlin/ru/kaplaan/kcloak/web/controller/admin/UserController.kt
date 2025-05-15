package ru.kaplaan.kcloak.web.controller.admin

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.service.core.UserService
import ru.kaplaan.kcloak.web.dto.UserDto

@RestController
@RequestMapping("/admin/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('READ_USERS')")
    fun getUserById(@PathVariable userId: Long): UserDto {
        return userService.getByUserId(userId)
    }

    @GetMapping("/all/{pageNumber}")
    @PreAuthorize("hasAuthority('READ_USERS')")
    fun getAllUsers(@Valid @Min(1) @PathVariable pageNumber: Int): List<UserDto> {
        return userService.getAll(pageNumber)
    }

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_USERS')")
    fun createUser(@Valid @RequestBody user: OneAccessUser): UserDto {
        return userService.create(user)
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('WRITE_USERS')")
    fun updateUser(@Valid @RequestBody user: OneAccessUser, @PathVariable userId: Long): UserDto {
        return userService.update(user, userId)
    }


}
