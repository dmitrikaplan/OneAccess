package ru.kaplaan.kcloak.web.controller.admin

import jakarta.validation.Valid
import jakarta.validation.constraints.Min
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.service.core.UserService

@RestController
@RequestMapping("/admin/users")
class UserController(
    private val userService: UserService,
) {

    @GetMapping("/{userId}")
    fun getUserById(@PathVariable userId: Long): OneAccessUser {
        return userService.getByUserId(userId)
    }

    @GetMapping("/all/{pageNumber}")
    fun getAllUsers(@Valid @Min(1) @PathVariable pageNumber: Int): List<OneAccessUser> {
        return userService.getAll(pageNumber)
    }

}
