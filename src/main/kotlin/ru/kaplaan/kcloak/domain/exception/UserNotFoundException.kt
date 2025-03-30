package ru.kaplaan.kcloak.domain.exception


class UserNotFoundException(
    override val message: String,
) : RuntimeException(message)