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
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.web.dto.RoleDto

@Component
class RoleSteps(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    fun findRole(cookie: Cookie?, roleId: Long, httpStatus: HttpStatus): RoleDto? {
        return mockMvc.get("/admin/roles/${roleId}") {
            if (cookie != null) {
                cookie(cookie)
            }
        }.chooseState<RoleDto>(httpStatus)
    }


    fun getAllRoles(cookie: Cookie?, httpStatus: HttpStatus, pageNumber: Int = 1): List<RoleDto> {
        return mockMvc.get("/admin/roles/all/$pageNumber") {
            if (cookie != null)
                cookie(cookie)
        }
            .chooseState<List<RoleDto>>(httpStatus) ?: listOf()
    }

    fun createRole(cookie: Cookie?, oneAccessRole: OneAccessRole, httpStatus: HttpStatus): RoleDto? {
        return mockMvc.post("/admin/roles") {
            if (cookie != null)
                cookie(cookie)

            content = objectMapper.writeValueAsString(oneAccessRole)
            contentType = MediaType.APPLICATION_JSON
            with(csrf())
        }.chooseState<RoleDto>(httpStatus)
    }

    fun updateRole(
        cookie: Cookie?,
        oneAccessRole: OneAccessRole,
        roleId: Long,
        httpStatus: HttpStatus,
    ): RoleDto? {
        return mockMvc.put("/admin/roles/$roleId") {
            if (cookie != null) cookie(cookie)

            content = objectMapper.writeValueAsString(oneAccessRole)
            contentType = MediaType.APPLICATION_JSON
            with(csrf())
        }.chooseState<RoleDto>(httpStatus)
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
        }.andReturn().let {
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