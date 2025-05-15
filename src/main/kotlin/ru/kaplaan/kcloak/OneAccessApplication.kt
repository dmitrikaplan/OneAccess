package ru.kaplaan.kcloak

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan
@EnableSpringHttpSession
class OneAccessApplication

fun main(args: Array<String>) {
    runApplication<OneAccessApplication>(*args)
}
