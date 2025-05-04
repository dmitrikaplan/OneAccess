package ru.kaplaan.kcloak.it.steps

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.stereotype.Service
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import ru.kaplaan.kcloak.config.properties.OneAccessClient
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.config.properties.OneAccessUser

@Service
class AdminSteps(
    private val mockMvc: MockMvc,
    private val objectMapper: ObjectMapper
) {

    fun findAllUsers(pageNumber: Int = 1): List<OneAccessUser> {
        return mockMvc.get("/admin/users/all/$pageNumber")
            .andExpectAll {
                status {
                    isOk()
                }
            }.andReturn()
            .let {
                objectMapper.readValue<List<OneAccessUser>>(it.response.contentAsString)
            }
    }

    fun getAllClients(pageNumber: Int = 1): List<OneAccessClient> {
        return mockMvc.get("/admin/clients/all/$pageNumber")
            .andExpect{
                status {
                    isOk()
                }
            }.andReturn()
            .let {
                objectMapper.readValue<List<OneAccessClient>>(it.response.contentAsString)
            }
    }


    fun getAllRoles(pageNumber: Int = 1): List<OneAccessRole> {
        return mockMvc.get("/admin/roles/all/$pageNumber")
            .andExpect{
                status {
                    isOk()
                }
            }.andReturn()
            .let {
                objectMapper.readValue<List<OneAccessRole>>(it.response.contentAsString)
            }
    }
}