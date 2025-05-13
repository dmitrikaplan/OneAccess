package ru.kaplaan.kcloak.it.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.junit.Assert.assertTrue
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.web.servlet.function.RequestPredicates.contentType
import org.springframework.web.util.UriComponentsBuilder
import ru.kaplaan.kcloak.it.core.creds.ClientCredentials
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.it.core.oidc.AccessTokenResponse
import java.util.*

@Component
class OidcSteps(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    fun loginViaAuthorizationCodeFlow(clientCredentials: ClientCredentials, errorExpected: Boolean = false): AccessTokenResponse? {

        val uri = UriComponentsBuilder.fromPath("/authorize")
            .queryParam("scope", OidcScopes.OPENID)
            .queryParam("response_type", "code")
            .queryParam("client_id", clientCredentials.clientId)
            .queryParam("redirect_uri", "http://localhost:5000/code")
            .build()
            .toUriString()

        val redirectUri = mockMvc.get(uri)
            .andExpect {
                status {
                    is3xxRedirection()
                }
            }
            .andReturn()
            .let {
                val redirectUri = checkNotNull(it.response.redirectedUrl)
                redirectUri
            }



        assertThat(redirectUri).contains("http://localhost:5000/code?code=")

        val queryParams =
            UriComponentsBuilder.fromUriString(redirectUri).build().queryParams
        val authorizationCode = checkNotNull(queryParams.getFirst("code"))
        assertThat(authorizationCode).isNotEmpty()


        val basicAuth = "Basic " + Base64.getEncoder()
            .encodeToString("${clientCredentials.clientId}:${clientCredentials.clientSecret}".toByteArray())

        return mockMvc.post("/token") {
            param("grant_type", "authorization_code")
            param("scope", OidcScopes.OPENID)
            param("code", authorizationCode)
            param("redirect_uri", "http://localhost:5000")
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

        }
            .andReturn()
            .let {
                if (errorExpected) {
                    return@let null
                }
                objectMapper.readValue<AccessTokenResponse>(it.response.contentAsString)
            }
    }


    fun loginViaClientCredentials(clientCredentials: ClientCredentials, errorExpected: Boolean): AccessTokenResponse? {

        val basicAuth = "Basic " + Base64.getEncoder()
            .encodeToString("${clientCredentials.clientId}:${clientCredentials.clientSecret}".toByteArray())

        return mockMvc.post("/token") {
            param("grant_type", "client_credentials")
            param("scope", "openid")
            param("redirect_uri", "http://localhost:5000")
            header("Authorization", basicAuth)
            contentType = MediaType.APPLICATION_FORM_URLENCODED
        }
            .andExpect {

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

            }
            .andReturn()
            .let {
                if (errorExpected) {
                    return@let null
                }
                objectMapper.readValue<AccessTokenResponse>(it.response.contentAsString)
            }
    }

    fun getOpenIdConfiguration(): OidcProviderConfiguration {
        return mockMvc.get("/.well-known/openid-configuration")
            .andExpect {
                status {
                    isOk()
                }
            }
            .andReturn()
            .let {
                objectMapper.readValue<OidcProviderConfiguration>(it.response.contentAsString)
            }
    }


}