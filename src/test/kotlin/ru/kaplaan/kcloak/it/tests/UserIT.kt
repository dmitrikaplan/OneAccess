package ru.kaplaan.kcloak.it.tests

import org.junit.Assert.*
import org.junit.Test
import org.springframework.http.HttpStatus
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.config.properties.OneAccessUser
import ru.kaplaan.kcloak.it.core.*
import ru.kaplaan.kcloak.it.core.creds.UserCredentials

class UserIT : TestIt() {

    @Test
    fun `admin can get all users`() = testIt {
        login(UserCredentials.SADMIN)
        val users = getAllUsers()
        assertTrue(users.isNotEmpty())
    }

    @Test
    fun `basic user can't get all users`() = testIt {
        login(UserCredentials.USER1)
        val users = getAllUsers(httpStatus = HttpStatus.FORBIDDEN)
        assertTrue(users.isEmpty())
    }

    @Test
    fun `not registered user can't get all users`() = testIt {
        login(UserCredentials(randomName(), randomPassword()), isErrorExpected = true)
        val users = getAllUsers(httpStatus = HttpStatus.FORBIDDEN)
        assertTrue(users.isEmpty())
    }

    @Test
    fun `admin can create and update user`() = testIt {
        login(UserCredentials.SADMIN)
        assertIsAuthorized()
        val role = createRole(OneAccessRole(randomRoleName()))!!

        val oneAccessUser = OneAccessUser(
            username = randomName(),
            email = randomEmail(),
            enabled = true,
            firstName = randomName(),
            lastName = randomName(),
            password = randomPassword(),
            roles = hashSetOf(role.name)
        )

        val user = createUser(
            oneAccessUser = oneAccessUser,
        ) {
            checkNotNull(this)
            assertEquals(oneAccessUser.username, username)
            assertEquals(oneAccessUser.email, email)
            assertEquals(oneAccessUser.enabled, enabled)
            assertEquals(oneAccessUser.firstName, firstName)
            assertEquals(oneAccessUser.lastName, lastName)
            assertEquals(oneAccessUser.roles, roles)
        }

        checkNotNull(user)
        val newEmail = randomEmail()
        val newUsername = randomName()

        updateUser(
            oneAccessUser = OneAccessUser(
                username = newUsername,
                email = newEmail,
                enabled = false,
                firstName = oneAccessUser.firstName,
                lastName = oneAccessUser.lastName,
                password = oneAccessUser.password,
                roles = oneAccessUser.roles
            ), userId = user.id
        ) {
            checkNotNull(this)
            assertEquals(newUsername, username)
            assertEquals(newEmail, email)
            assertEquals(false, enabled)
            assertEquals(oneAccessUser.firstName, firstName)
            assertEquals(oneAccessUser.lastName, lastName)
            assertEquals(oneAccessUser.roles, roles)
        }
    }

    @Test
    fun `basic user can't create client`() = testIt {
        login(UserCredentials.USER1)
        assertIsAuthorized()

        val oneAccessUser = OneAccessUser(
            username = randomName(),
            email = randomEmail(),
            enabled = true,
            firstName = randomName(),
            lastName = randomName(),
            password = randomPassword(),
            roles = hashSetOf("USER")
        )

        val user = createUser(
            oneAccessUser = oneAccessUser,
            httpStatus = HttpStatus.FORBIDDEN,
        ) {
            assertNull(this)
        }
    }
}