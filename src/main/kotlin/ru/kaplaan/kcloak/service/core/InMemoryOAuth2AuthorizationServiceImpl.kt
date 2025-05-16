package ru.kaplaan.kcloak.service.core

import org.springframework.lang.Nullable
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2DeviceCode
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.OAuth2UserCode
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationCode
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.util.Assert
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.function.Consumer

class InMemoryOAuth2AuthorizationServiceImpl : OAuth2AuthorizationService {
    private var maxInitializedAuthorizations = 100

    /*
	 * Stores "initialized" (uncompleted) authorizations, where an access token has not
	 * yet been granted. This state occurs with the authorization_code grant flow during
	 * the user consent step OR when the code is returned in the authorization response
	 * but the access token request is not yet initiated.
	 */
    private var initializedAuthorizations: MutableMap<String, OAuth2Authorization> =
        ConcurrentHashMap(MaxSizeHashMap(this.maxInitializedAuthorizations))

    /*
	 * Stores "completed" authorizations, where an access token has been granted.
	 */
    private val authorizations: MutableMap<String, OAuth2Authorization> = ConcurrentHashMap()

    /*
	 * Constructor used for testing only.
	 */
    internal constructor(maxInitializedAuthorizations: Int) {
        this.maxInitializedAuthorizations = maxInitializedAuthorizations
        this.initializedAuthorizations = Collections
            .synchronizedMap(MaxSizeHashMap(this.maxInitializedAuthorizations))
    }

    /**
     * Constructs an `InMemoryOAuth2AuthorizationService` using the provided
     * parameters.
     * @param authorizations the authorization(s)
     */
    constructor(vararg authorizations: OAuth2Authorization?) : this(Arrays.asList<OAuth2Authorization>(*authorizations))

    /**
     * Constructs an `InMemoryOAuth2AuthorizationService` using the provided
     * parameters.
     * @param authorizations the authorization(s)
     */
    /**
     * Constructs an `InMemoryOAuth2AuthorizationService`.
     */
    @JvmOverloads
    constructor(authorizations: List<OAuth2Authorization> = emptyList()) {
        Assert.notNull(authorizations, "authorizations cannot be null")
        authorizations.forEach(Consumer { authorization: OAuth2Authorization ->
            Assert.notNull(authorization, "authorization cannot be null")
            Assert.isTrue(
                !this.authorizations.containsKey(authorization.id),
                "The authorization must be unique. Found duplicate identifier: " + authorization.id
            )
            this.authorizations[authorization.id] = authorization
        })
    }

    override fun save(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")
        if (isComplete(authorization)) {
            authorizations[authorization.id] = authorization
        } else {
            initializedAuthorizations[authorization.id] = authorization
        }
    }

    override fun remove(authorization: OAuth2Authorization) {
        Assert.notNull(authorization, "authorization cannot be null")
        if (isComplete(authorization)) {
            authorizations.remove(authorization.id, authorization)
        } else {
            initializedAuthorizations.remove(authorization.id, authorization)
        }
    }

    @Nullable
    override fun findById(id: String): OAuth2Authorization? {
        Assert.hasText(id, "id cannot be empty")
        val authorization = authorizations[id]
        return if ((authorization != null)) authorization else initializedAuthorizations[id]
    }

    @Nullable
    override fun findByToken(token: String, @Nullable tokenType: OAuth2TokenType?): OAuth2Authorization? {
        Assert.hasText(token, "token cannot be empty")
        for (authorization in authorizations.values) {
            if (hasToken(authorization, token, tokenType)) {
                return authorization
            }
        }
        for (authorization in initializedAuthorizations.values) {
            if (hasToken(authorization, token, tokenType)) {
                return authorization
            }
        }
        return null
    }

    private class MaxSizeHashMap<K, V>(private val maxSize: Int) : LinkedHashMap<K, V>() {
        override fun removeEldestEntry(eldest: Map.Entry<K, V>): Boolean {
            return size > this.maxSize
        }
    }

    companion object {
        private fun isComplete(authorization: OAuth2Authorization): Boolean {
            return authorization.accessToken != null
        }

        private fun hasToken(
            authorization: OAuth2Authorization, token: String,
            @Nullable tokenType: OAuth2TokenType?,
        ): Boolean {
            // @formatter:off
            if (tokenType == null) {
                return matchesState(authorization, token) ||
                        matchesAuthorizationCode(authorization, token) ||
                        matchesAccessToken(authorization, token) ||
                        matchesIdToken(authorization, token) ||
                        matchesRefreshToken(authorization, token) ||
                        matchesDeviceCode(authorization, token) ||
                        matchesUserCode(authorization, token)
            }
            else if (OAuth2ParameterNames.STATE == tokenType.value) {
                return matchesState(authorization, token)
            }
            else if (OAuth2ParameterNames.CODE == tokenType.value) {
                return matchesAuthorizationCode(authorization, token)
            }
            else if (OAuth2TokenType.ACCESS_TOKEN == tokenType) {
                return matchesAccessToken(authorization, token)
            }
            else if (OidcParameterNames.ID_TOKEN == tokenType.value) {
                return matchesIdToken(authorization, token)
            }
            else if (OAuth2TokenType.REFRESH_TOKEN == tokenType) {
                return matchesRefreshToken(authorization, token)
            }
            else if (OAuth2ParameterNames.DEVICE_CODE == tokenType.value) {
                return matchesDeviceCode(authorization, token)
            }
            else if (OAuth2ParameterNames.USER_CODE == tokenType.value) {
                return matchesUserCode(authorization, token)
            }
            // @formatter:on
            return false
        }

        private fun matchesState(authorization: OAuth2Authorization, token: String): Boolean {
            return token == authorization.getAttribute(OAuth2ParameterNames.STATE)
        }

        private fun matchesAuthorizationCode(authorization: OAuth2Authorization, token: String): Boolean {
            val authorizationCode = authorization
                .getToken(OAuth2AuthorizationCode::class.java)
            return authorizationCode != null && authorizationCode.token.tokenValue == token
        }

        private fun matchesAccessToken(authorization: OAuth2Authorization, token: String): Boolean {
            val accessToken = authorization.getToken(
                OAuth2AccessToken::class.java
            )
            return accessToken != null && accessToken.token.tokenValue == token
        }

        private fun matchesRefreshToken(authorization: OAuth2Authorization, token: String): Boolean {
            val refreshToken = authorization.getToken(
                OAuth2RefreshToken::class.java
            )
            return refreshToken != null && refreshToken.token.tokenValue == token
        }

        private fun matchesIdToken(authorization: OAuth2Authorization, token: String): Boolean {
            val idToken = authorization.getToken(OidcIdToken::class.java)
            return idToken != null && idToken.token.tokenValue == token
        }

        private fun matchesDeviceCode(authorization: OAuth2Authorization, token: String): Boolean {
            val deviceCode = authorization.getToken(
                OAuth2DeviceCode::class.java
            )
            return deviceCode != null && deviceCode.token.tokenValue == token
        }

        private fun matchesUserCode(authorization: OAuth2Authorization, token: String): Boolean {
            val userCode = authorization.getToken(
                OAuth2UserCode::class.java
            )
            return userCode != null && userCode.token.tokenValue == token
        }
    }
}