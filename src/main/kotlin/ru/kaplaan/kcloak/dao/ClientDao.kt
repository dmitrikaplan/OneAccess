package ru.kaplaan.kcloak.dao

import org.jooq.DSLContext
import org.springframework.stereotype.Component
import ru.kaplaan.kcloak.jooq.tables.Oauth2RegisteredClient


private val CLIENT: Oauth2RegisteredClient = Oauth2RegisteredClient.OAUTH2_REGISTERED_CLIENT

@Component
class ClientDao(
    private val db: DSLContext,
) {


    fun getAllClientIds(pageNumber: Int, pageSize: Int): List<String> {
        val from = (pageNumber - 1) * pageSize
        return db.select(CLIENT.CLIENT_ID).from(CLIENT)
            .offset(from)
            .limit(pageSize)
            .fetchInto(String::class.java)
    }

}