package ru.kaplaan.kcloak.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository


@Configuration
class AuthorizationServerConfig {

//    @Bean
//    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
//        return http.exceptionHandling { exceptions ->
//            exceptions.authenticationEntryPoint(
//                LoginUrlAuthenticationEntryPoint("/login")
//            )
//        }.build()
//    }


    @Bean
    fun registeredClientRepository(jdbcOperations: JdbcOperations): RegisteredClientRepository {
        return JdbcRegisteredClientRepository(jdbcOperations)
    }

//    @Bean
//    fun jwkSource(): JWKSource<SecurityContext> {
//        val rsaKey: RSAKey = JwkUtils.generateRsa()
//        val jwkSet = JWKSet(rsaKey)
//        return JWKSource<SecurityContext> { jwkSelector: JWKSelector, _: SecurityContext? ->
//            jwkSelector.select(
//                jwkSet
//            )
//        }
//    }
}