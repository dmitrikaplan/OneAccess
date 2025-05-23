package ru.kaplaan.kcloak.it.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import jakarta.servlet.http.Cookie
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.stereotype.Component
import org.springframework.test.web.servlet.*
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.web.dto.UserDto

@Component
class UserSteps(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    fun findUser(cookie: Cookie?, userId: Long, httpStatus: HttpStatus): UserDto? {
        return mockMvc.get("/admin/users/${userId}") {
            if (cookie != null) {
                cookie(cookie)
            }
        }
            .chooseState<UserDto>(httpStatus)
    }


    fun getAllUsers(cookie: Cookie?, httpStatus: HttpStatus, pageNumber: Int = 1): List<UserDto> {
        return mockMvc.get("/admin/users/all/$pageNumber") {
            if (cookie != null)
                cookie(cookie)
        }
            .chooseState<List<UserDto>>(httpStatus) ?: listOf()
    }

    fun createUser(cookie: Cookie?, oneAccessUser: OneAccessUser, httpStatus: HttpStatus): UserDto? {
        return mockMvc.post("/admin/users") {
            if (cookie != null) cookie(cookie)

            content = objectMapper.writeValueAsString(oneAccessUser)
            contentType = MediaType.APPLICATION_JSON
            with(csrf())
        }.chooseState<UserDto>(httpStatus)
    }

    fun updateUser(
        cookie: Cookie?,
        oneAccessUser: OneAccessUser,
        userId: Long,
        httpStatus: HttpStatus,
    ): UserDto? {
        return mockMvc.put("/admin/users/$userId") {
            if (cookie != null) cookie(cookie)

            content = objectMapper.writeValueAsString(oneAccessUser)
            contentType = MediaType.APPLICATION_JSON
            with(csrf())
        }.chooseState<UserDto>(httpStatus)
    }


    private inline fun <reified T> ResultActionsDsl.chooseState(statusCode: HttpStatusCode): T? {
        return when (statusCode) {
            HttpStatus.OK -> successFullRequest<T>()
            else -> failedRequest<T>()
        }
    }


    private inline fun <reified T> ResultActionsDsl.successFullRequest(): T {
        return andExpect {
            status {
                isOk()
            }
        }
            .andReturn()
            .let {
                objectMapper.readValue<T>(it.response.contentAsString)
            }
    }

    private inline fun <reified T> ResultActionsDsl.failedRequest(): T? {
        andExpect {
            status {
                is4xxClientError()
            }
        }

        return null
    }

}