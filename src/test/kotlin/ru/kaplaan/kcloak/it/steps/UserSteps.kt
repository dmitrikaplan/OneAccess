package ru.kaplaan.kcloak.it.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.Cookie
import org.junit.Assert.assertNotNull
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.stereotype.Service
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import ru.kaplaan.kcloak.it.core.oidc.AccessTokenResponse
import ru.kaplaan.kcloak.it.core.creds.UserCredentials
import ru.kaplaan.kcloak.web.dto.ClientDto
import ru.kaplaan.kcloak.web.dto.RoleDto
import ru.kaplaan.kcloak.web.dto.User


@Service
class UserSteps(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    private var cookie: Cookie? = null

    fun login(userCredentials: UserCredentials) {
        mockMvc.post("/login") {
            param("username", userCredentials.email)
            param("password", userCredentials.password)
            with(csrf())
        }.andExpect {
            status {
                is3xxRedirection()
            }
        }
    }

    fun findAllUsers(accessToken: AccessTokenResponse, pageNumber: Int = 1): List<User> {
        return mockMvc.get("/admin/users/all/$pageNumber") {
            cookie(checkNotNull(cookie))
            header("Authorization", accessToken.tokenType + " " + accessToken.tokenValue)
        }
            .andExpectAll {
                status {
                    isOk()
                }
            }.andReturn()
            .let {
                objectMapper.readValue<List<User>>(it.response.contentAsString)
            }
    }

    fun getAllClients(accessToken: AccessTokenResponse, pageNumber: Int = 1): List<ClientDto> {
        return mockMvc.get("/admin/clients/all/$pageNumber") {
            cookie(checkNotNull(cookie))
            header("Authorization", accessToken.tokenType + " " + accessToken.tokenValue)
        }
            .andExpect {
                status {
                    isOk()
                }
            }.andReturn()
            .let {
                objectMapper.readValue<List<ClientDto>>(it.response.contentAsString)
            }
    }


    fun getAllRoles(accessToken: AccessTokenResponse, pageNumber: Int = 1): List<RoleDto> {
        return mockMvc.get("/admin/roles/all/$pageNumber") {
            cookie(checkNotNull(cookie))
            header("Authorization", accessToken.tokenType + " " + accessToken.tokenValue)
        }
            .andExpect {
                status {
                    isOk()
                }
            }.andReturn()
            .let {
                objectMapper.readValue<List<RoleDto>>(it.response.contentAsString)
            }
    }

    fun assertCookieNotNull() {
        assertNotNull(cookie)
    }
}