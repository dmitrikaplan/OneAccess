package ru.kaplaan.kcloak.config.security

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.Customizer.withDefaults
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.NoOpPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.context.NullSecurityContextRepository
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher
import org.springframework.security.web.util.matcher.OrRequestMatcher
import org.springframework.session.MapSessionRepository
import org.springframework.session.Session
import ru.kaplaan.kcloak.service.core.UserService
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.*
import java.util.concurrent.ConcurrentHashMap


private val publicPaths = arrayOf(
    "/public/**",
    "/login",
    "/login/**",
    "/actuator/**"
)

private val adminEndpoints = listOf(
    antMatcher("/admin/**")
)

@Configuration
@EnableMethodSecurity(prePostEnabled = true)
class SecurityConfig {

    @Bean
    @Order(1)
    fun adminFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http
            .securityMatcher(OrRequestMatcher(adminEndpoints))
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .disableStandardFilters()
            .build()
    }

    @Bean
    @Order(2)
    fun authServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {

        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer.authorizationServer()
        http.securityMatcher(authorizationServerConfigurer.endpointsMatcher)
            .with(authorizationServerConfigurer) {
                it.oidc(withDefaults())
            }
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .disableStandardFilters()
        return http.build()
    }

    @Bean
    @Order(3)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain {
        return http.authorizeHttpRequests {
            it.requestMatchers(*publicPaths).permitAll()
        }
            .basicSettings()
            .build()
    }

    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val keyPair: KeyPair = generateRsaKey()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        val rsaKey: RSAKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
        val jwkSet = JWKSet(rsaKey)
        return ImmutableJWKSet(jwkSet)
    }

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    @Bean
    fun userDetailsService(userService: UserService): UserDetailsService {
        return UserDetailsService {
            userService.findByEmail(it)
        }
    }

    @Bean
    fun sessionRepository(): MapSessionRepository {
        return MapSessionRepository(ConcurrentHashMap<String, Session>())
    }


    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return NoOpPasswordEncoder.getInstance()
    }

    private fun generateRsaKey(): KeyPair {
        val keyPair = try {
            val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
            keyPairGenerator.initialize(2048)
            keyPairGenerator.generateKeyPair()
        } catch (e: Exception) {
            throw IllegalStateException(e)
        }
        return keyPair
    }


    private fun HttpSecurity.basicSettings(): HttpSecurity {
        return this.formLogin { form ->
            form
                .successHandler { request, response, _ ->
                    response.status = 200
                    request.getSession(true)
                }
        }
            .rememberMe {
                it.key("KEY")
                it.tokenValiditySeconds(86400)
            }
    }

    private fun HttpSecurity.disableStandardFilters(): HttpSecurity {
        return httpBasic { it.disable() }
            .csrf { it.disable() }
            .formLogin { it.disable() }
            .logout { it.disable() }
            .cors { it.disable() }
            .sessionManagement { it.disable() }
            .requestCache { it.disable() }
            .servletApi { it.disable() }
            .anonymous { it.disable() }
            .securityContext {
                it.securityContextRepository(NullSecurityContextRepository())
            }
    }
}