package ru.kaplaan.kcloak.config.properties

enum class SupportedResponseTypesProperties(val value: String) {
    CODE("code"), ID_TOKEN("id_token"), TOKEN("token"),
    ID_TOKEN_TOKEN("id_token token"), CODE_ID_TOKEN("code id_token"),
    CODE_TOKEN("code token"), CODE_ID_TOKEN_TOKEN("code id_token token");

    companion object {
        fun getSupportedResponseTypes(): Set<String> {
            return entries.map { it.value }.toSet()
        }
    }

}