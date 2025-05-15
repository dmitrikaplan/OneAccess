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
import ru.kaplaan.kcloak.web.dto.UserDto


@Service
class UserSteps(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {


    fun login(userCredentials: UserCredentials): Cookie {
        return mockMvc.post("/login") {
            param("username", userCredentials.email)
            param("password", userCredentials.password)
            with(csrf())
        }.andExpect {
            status {
                isOk()
            }
            cookie {
                exists("SESSION")
            }
        }.andReturn()
            .let {
                checkNotNull(it.response.getCookie("SESSION"))
            }

    }

    fun findAllUsers(cookie: Cookie, pageNumber: Int = 1): List<UserDto> {
        return mockMvc.get("/admin/users/all/$pageNumber") {
            cookie(cookie)
        }
            .andExpectAll {
                status {
                    isOk()
                }
            }.andReturn()
            .let {
                objectMapper.readValue<List<UserDto>>(it.response.contentAsString)
            }
    }

    fun getAllClients(cookie: Cookie, pageNumber: Int = 1): List<ClientDto> {
        return mockMvc.get("/admin/clients/all/$pageNumber") {
            cookie(cookie)
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


    fun getAllRoles(cookie: Cookie, pageNumber: Int = 1): List<RoleDto> {
        return mockMvc.get("/admin/roles/all/$pageNumber") {
            cookie(cookie)
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

}