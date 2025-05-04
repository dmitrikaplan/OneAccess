package ru.kaplaan.kcloak.web.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/public")
class SystemController {


    @GetMapping("/healthCheck")
    fun healthCheck(): String {
        return "OK"
    }
}