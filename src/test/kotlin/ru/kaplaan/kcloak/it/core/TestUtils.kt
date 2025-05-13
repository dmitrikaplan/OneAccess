package ru.kaplaan.kcloak.it.core

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

fun randomUsername(): String {
    return randomAlphanumeric(20)
}

fun randomPassword(): String {
    return randomAlphanumeric(20)
}