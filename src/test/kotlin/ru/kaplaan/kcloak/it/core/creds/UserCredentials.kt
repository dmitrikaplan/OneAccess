package ru.kaplaan.kcloak.it.core.creds

open class UserCredentials(val email: String, val password: String) {
    object SADMIN: UserCredentials("superadmin@test1.com", "superadmin")
    object USER1: UserCredentials("user1@test1.com", "123")
    object USER2: UserCredentials("user2@test1.com", "123")
}