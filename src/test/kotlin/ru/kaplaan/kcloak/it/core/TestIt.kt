package ru.kaplaan.kcloak.it.core

import org.junit.runner.RunWith
import org.springframework.beans.factory.ObjectFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import ru.kaplaan.kcloak.config.TestConfig

@RunWith(SpringRunner::class)
@ActiveProfiles("test", "test-realm", "test-local")
@SpringBootTest(
//    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = [
        TestConfig::class
    ]
)
@AutoConfigureMockMvc
abstract class TestIt{

    @Autowired
    lateinit var testContextFactory: ObjectFactory<TestItContextImpl>

    protected fun testIt(run: TestItContext.() -> Unit) {
        testContextFactory.`object`.run()
    }
}