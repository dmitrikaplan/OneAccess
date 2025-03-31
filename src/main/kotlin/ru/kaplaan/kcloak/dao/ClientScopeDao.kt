package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.springframework.stereotype.Component

@Component
class ClientScopeDao(
    private val db: DSLContext
) {


}