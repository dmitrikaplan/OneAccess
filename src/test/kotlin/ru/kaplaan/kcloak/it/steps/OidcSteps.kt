package ru.kaplaan.kcloak.it.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.Cookie
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.util.UriComponentsBuilder
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.oidc.AuthorizationCodeFlowErrorType
import ru.kaplaan.kcloak.it.core.oidc.TokenResponse
import java.util.*

@Component
class OidcSteps(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    fun loginViaAuthorizationCodeFlow(
        clientCredentials: ClientCredentials,
        cookie: Cookie?,
        authorizationCodeFlowErrorType: AuthorizationCodeFlowErrorType?,
    ): TokenResponse? {

        val uri = UriComponentsBuilder.fromPath("/authorize").queryParam("scope", clientCredentials.scopesToString())
            .queryParam("response_type", "code").queryParam("client_id", clientCredentials.clientId)
            .queryParam("redirect_uri", clientCredentials.redirectUri).build().toUriString()

        val redirectUri = mockMvc.get(uri) {
            if (cookie != null) cookie(cookie)
        }.andExpect {

            if (authorizationCodeFlowErrorType == AuthorizationCodeFlowErrorType.INVALID_CLIENT_ID) {
                status {
                    isBadRequest()
                }
            } else {
                status {
                    is3xxRedirection()
                }

            }
        }.andReturn().let {
            if (authorizationCodeFlowErrorType == AuthorizationCodeFlowErrorType.INVALID_CLIENT_ID) return null

            val redirectUri = checkNotNull(it.response.redirectedUrl)
            redirectUri
        }



        assertThat(redirectUri).contains("${clientCredentials.redirectUri}?code=")

        val queryParams = UriComponentsBuilder.fromUriString(redirectUri).build().queryParams
        val authorizationCode = checkNotNull(queryParams.getFirst("code"))
        assertThat(authorizationCode).isNotEmpty()


        val basicAuth = "Basic " + Base64.getEncoder()
            .encodeToString("${clientCredentials.clientId}:${clientCredentials.clientSecret}".toByteArray())

        return mockMvc.post("/token") {
            param("grant_type", "authorization_code")
            param("scope", clientCredentials.scopesToString())
            param("code", authorizationCode)
            param("redirect_uri", clientCredentials.redirectUri)
            header("Authorization", basicAuth)
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }.andExpect {

            if (authorizationCodeFlowErrorType == AuthorizationCodeFlowErrorType.INVALID_CLIENT_SECRET) {
                status {
                    isUnauthorized()
                }

                jsonPath("$.access_token") {
                    doesNotExist()
                }
            } else {
                status {
                    isOk()
                }

                jsonPath("$.access_token") {
                    exists()
                }
                jsonPath("$.token_type") {
                    value("Bearer")
                }
            }

        }.andReturn().let {

            if (authorizationCodeFlowErrorType == AuthorizationCodeFlowErrorType.INVALID_CLIENT_SECRET) return null

            objectMapper.readValue<TokenResponse>(it.response.contentAsString)
        }
    }


    fun loginViaClientCredentials(clientCredentials: ClientCredentials, errorExpected: Boolean): TokenResponse? {

        val basicAuth = "Basic " + Base64.getEncoder()
            .encodeToString("${clientCredentials.clientId}:${clientCredentials.clientSecret}".toByteArray())

        return mockMvc.post("/token") {
            param("grant_type", "client_credentials")
            param("scope", clientCredentials.scopesToString())
            param("redirect_uri", clientCredentials.redirectUri)
            header("Authorization", basicAuth)
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }.andExpect {

            if (!errorExpected) {
                status {
                    isOk()
                }

                jsonPath("$.access_token") {
                    exists()
                }
                jsonPath("$.token_type") {
                    value("Bearer")
                }
            } else {
                status {
                    is4xxClientError()
                }
            }

        }.andReturn().let {
            if (errorExpected) {
                return@let null
            }
            objectMapper.readValue<TokenResponse>(it.response.contentAsString)
        }
    }

    fun getOpenIdConfiguration(): OidcProviderConfiguration {
        return mockMvc.get("/.well-known/openid-configuration").andExpect {
            status {
                isOk()
            }
        }.andReturn().let {
            objectMapper.readValue<OidcProviderConfiguration>(it.response.contentAsString)
        }
    }

    fun updateAccessToken(
        clientCredentials: ClientCredentials,
        cookie: Cookie?,
        refreshToken: String,
        isErrorExpected: Boolean,
    ): TokenResponse? {
        return mockMvc.post("/token") {
            param("grant_type", "refresh_token")
            param("client_id", clientCredentials.clientId)
            param("client_secret", clientCredentials.clientSecret)
            param("scope", clientCredentials.scopesToString())
            param("refresh_token", refreshToken)

            if (cookie != null) cookie(cookie)
            contentType = MediaType.APPLICATION_FORM_URLENCODED

        }.andExpect {

            if (isErrorExpected) {
                status {
                    isBadRequest()
                }
                jsonPath("$.access_token") {
                    doesNotExist()
                }

            } else {
                status {
                    isOk()
                }

                jsonPath("$.access_token") {
                    exists()
                }
            }

        }.andReturn().let {
            if (isErrorExpected) return@let null

            checkNotNull(
                objectMapper.readValue<TokenResponse>(it.response.contentAsString)
            )
        }
    }

    fun loginViaPasswordFlow(
        userCredentials: UserCredentials,
        clientCredentials: ClientCredentials,
        isErrorExpected: Boolean,
    ): TokenResponse? {
        return mockMvc.post("/token") {
            param("grant_type", "password")
            param("username", userCredentials.email)
            param("password", userCredentials.password)
            param("client_id", clientCredentials.clientId)
            param("client_secret", clientCredentials.clientSecret)
        }.andExpect {

            if (isErrorExpected) {
                status {
                    isBadRequest()
                }

                jsonPath("$.access_token") {
                    doesNotExist()
                }
            } else {
                status {
                    isOk()
                }

                jsonPath("$.access_token") {
                    exists()
                }
            }
        }.andReturn().let {
            if (isErrorExpected) return null
            objectMapper.readValue<TokenResponse>(it.response.contentAsString)
        }
    }


}