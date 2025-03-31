package ru.kaplaan.kcloak.web.dto

data class JwksCerts(
    val keys: List<OneAccessCertificate>
)

data class OneAccessCertificate(
    val kid: String,
    val kty: String,
    val alg: String,
    val use: String,
    val n: String,
    val e: String,
)