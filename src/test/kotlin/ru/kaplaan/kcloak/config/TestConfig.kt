package ru.kaplaan.kcloak.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.web.context.WebApplicationContext

@TestConfiguration
class TestConfig(
    private var wac: WebApplicationContext
) {

//    @Bean
//    fun mockMvc(): MockMvc {
//        return MockMvcBuilders.webAppContextSetup(wac)
//            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
//            .build()
//    }
}