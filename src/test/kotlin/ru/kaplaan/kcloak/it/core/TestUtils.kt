package ru.kaplaan.kcloak.it.core

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphabetic
import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric
import java.util.*
import kotlin.collections.HashSet

fun randomName(): String {
    return randomAlphanumeric(20)
}

fun randomEmail(domain: String = "gmail.com"): String {
    return "${randomName()}@$domain"
}

fun randomPassword(): String {
    return randomAlphanumeric(20)
}

fun randomUri(): String {
    return "http://localhost:5000"
}

fun randomRoleName(): String {
    return randomAlphabetic(10).uppercase()
}

fun randomRoles(count: Int = 3): Set<String> {
    return (1..count).map { randomRoleName() }.toSet()
}

fun randomPermissions(count: Int = 5): HashSet<String> {
    return (1..count).map {
        randomPermission()
    }.toHashSet()
}

fun randomPermission(): String {
    return randomAlphabetic(10).lowercase()
}

fun randomUUID(): String {
    return UUID.randomUUID().toString()
}