package ru.kaplaan.kcloak.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.core.JdbcOperations
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import ru.kaplaan.kcloak.dao.CachedRegisteredClientRepository
import ru.kaplaan.kcloak.service.core.InMemoryOAuth2AuthorizationServiceImpl


@Configuration
class AuthorizationServerConfig {

    @Bean
    fun registeredClientRepository(jdbcOperations: JdbcOperations): RegisteredClientRepository {
        return CachedRegisteredClientRepository(jdbcOperations, 100)
    }

    @Bean
    fun inMemoryOauth2AuthorizationService(): OAuth2AuthorizationService {
        return InMemoryOAuth2AuthorizationServiceImpl()
    }

    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        return AuthorizationServerSettings.builder().issuer("http://localhost:8080").authorizationEndpoint("/authorize")
            .deviceAuthorizationEndpoint("/device-auth").deviceVerificationEndpoint("/device-verification")
            .tokenEndpoint("/token").tokenIntrospectionEndpoint("/introspect").tokenRevocationEndpoint("/revoke")
            .jwkSetEndpoint("/jwks").oidcLogoutEndpoint("/oidc/logout").oidcUserInfoEndpoint("/oidc/userinfo")
            .oidcClientRegistrationEndpoint("/oidc/register").build()
    }
}