package ru.kaplaan.kcloak.it.steps

import jakarta.servlet.http.Cookie
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.stereotype.Service
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.post
import ru.kaplaan.kcloak.it.core.creds.UserCredentials


@Service
class AuthSteps(
    private val mockMvc: MockMvc,
) {

    fun login(userCredentials: UserCredentials, isErrorExpected: Boolean): Cookie? {
        return mockMvc.post("/login") {
            param("username", userCredentials.email)
            param("password", userCredentials.password)
            with(csrf())
        }.andExpect {

            if (isErrorExpected) {
                status {
                    is3xxRedirection()
                }
            } else {
                cookie {
                    exists("SESSION")
                }

                status {
                    isOk()
                }
            }
        }.andReturn()
            .let {
                if (isErrorExpected)
                    null
                else
                    checkNotNull(it.response.getCookie("SESSION"))
            }

    }

}