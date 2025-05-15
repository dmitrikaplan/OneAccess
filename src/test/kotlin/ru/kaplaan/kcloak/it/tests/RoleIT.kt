package ru.kaplaan.kcloak.it.tests

import org.junit.Assert.*
import org.junit.Test
import org.springframework.http.HttpStatus
import ru.kaplaan.kcloak.config.properties.OneAccessRole
import ru.kaplaan.kcloak.it.core.*
import ru.kaplaan.kcloak.it.core.creds.UserCredentials

class RoleIT : TestIt() {


    @Test
    fun `admin can get all roles`() = testIt {
        login(UserCredentials.SADMIN)
        val roles = getAllRoles()
        assertTrue(roles.isNotEmpty())
    }

    @Test
    fun `basic user can't get all roles`() = testIt {
        login(UserCredentials.USER1)
        val roles = getAllRoles(httpStatus = HttpStatus.FORBIDDEN)
        assertTrue(roles.isEmpty())
    }

    @Test
    fun `not registered user can't get all roles`() = testIt {
        login(UserCredentials(randomName(), randomPassword()), isErrorExpected = true)
        val roles = getAllRoles(httpStatus = HttpStatus.FORBIDDEN)
        assertTrue(roles.isEmpty())
    }

    @Test
    fun `admin can create and update role`() = testIt {
        login(UserCredentials.SADMIN)
        assertIsAuthorized()
        val roleName = randomRoleName()
        val rolePermissions = randomPermissions()

        val role = createRole(
            oneAccessRole = OneAccessRole(roleName, rolePermissions)
        ) {
            checkNotNull(this)
            assertEquals(roleName, this.name)
            assertEquals(rolePermissions, this.permissions)
        }

        val newRolePermissions = randomPermissions()
        val roleId = checkNotNull(role?.roleId)

        updateRole(
            oneAccessRole = OneAccessRole(roleName, newRolePermissions),
            roleId = roleId
        ) {
            checkNotNull(this)
            assertEquals(roleName, this.name)
            assertEquals(newRolePermissions, this.permissions)
            assertEquals(roleId, this.roleId)
        }
    }

    @Test
    fun `basic user can't create role`() = testIt {
        login(UserCredentials.USER1)
        assertIsAuthorized()
        val roleName = randomRoleName()
        val rolePermissions = randomPermissions()

        createRole(
            oneAccessRole = OneAccessRole(roleName, rolePermissions),
            httpStatus = HttpStatus.FORBIDDEN
        ) {
            assertNull(this)
        }
    }

}