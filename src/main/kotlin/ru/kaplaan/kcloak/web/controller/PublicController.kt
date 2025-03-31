package ru.kaplaan.kcloak.web.controller

import jakarta.servlet.http.HttpServletRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ru.kaplaan.kcloak.service.OpenIdService
import ru.kaplaan.kcloak.web.dto.JwksCerts
import ru.kaplaan.kcloak.web.dto.OpenIdConfiguration

@RestController
class PublicController(
    private val openIdService: OpenIdService
) {

    @GetMapping("/realms/{realm}/.well-known/openid-configuration")
    fun getOpenIdConfiguration(@PathVariable realm: String, request: HttpServletRequest): OpenIdConfiguration {
        val baseUri = getBaseUri(request)
        return openIdService.getOpenIdConfiguration(realm, baseUri)
    }

    @PostMapping("/realms/{realm}/protocol/openid-connect/auth")
    fun authorization(@PathVariable realm: String) {
        //TODO: authorization code flow
    }

    @PostMapping("/realms/{realm}/protocol/openid-connect/token")
    fun token(@PathVariable realm: String) {

    }

    @PostMapping("/realms/{realm}/protocol/openid-connect/userinfo")
    fun userInfo(@PathVariable realm: String) {

    }

    @GetMapping("/realms/{realm}/protocol/openid-connect/certs")
    fun getJwksCerts(@PathVariable realm: String): JwksCerts {
        return openIdService.getJwksCerts(realm)
    }


    private fun getBaseUri(request: HttpServletRequest): String {
        val scheme = request.scheme
        val serverName = request.serverName
        val serverPort = request.serverPort
        var url = "$scheme://$serverName"
        if ((scheme == "http" && serverPort != 80)) {
            url += ":$serverPort"
        }
        return url
    }
}