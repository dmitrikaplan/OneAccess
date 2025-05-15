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
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.web.dto.ClientDto

@Component
class ClientSteps(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper,
) {

    fun findClient(cookie: Cookie?, clientId: Long, httpStatus: HttpStatus): ClientDto? {
        return mockMvc.get("/admin/clients/${clientId}") {
            if (cookie != null) {
                cookie(cookie)
            }
        }.chooseState<ClientDto>(httpStatus)
    }

    fun getAllClients(cookie: Cookie?, httpStatus: HttpStatus, pageNumber: Int = 1): List<ClientDto> {
        return mockMvc.get("/admin/clients/all/$pageNumber") {
            if (cookie != null) {
                cookie(cookie)
            }
        }.chooseState<List<ClientDto>>(httpStatus) ?: listOf()
    }

    fun createClient(cookie: Cookie?, oneAccessClient: OneAccessClient, httpStatus: HttpStatus): ClientDto? {
        return mockMvc.post("/admin/clients") {
            if (cookie != null) cookie(cookie)

            content = objectMapper.writeValueAsString(oneAccessClient)
            contentType = MediaType.APPLICATION_JSON
            with(csrf())
        }.chooseState<ClientDto>(httpStatus)
    }

    fun updateClient(
        cookie: Cookie?,
        oneAccessClient: OneAccessClient,
        clientId: String,
        httpStatus: HttpStatus,
    ): ClientDto? {
        return mockMvc.put("/admin/clients/$clientId") {
            if (cookie != null) cookie(cookie)

            content = objectMapper.writeValueAsString(oneAccessClient)
            contentType = MediaType.APPLICATION_JSON
            with(csrf())
        }.chooseState<ClientDto>(httpStatus)
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