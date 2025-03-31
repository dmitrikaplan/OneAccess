package ru.kaplaan.kcloak.web.dto

data class OpenIdConfiguration(
    val issuer: String,
    val authorizationEndpoint: String,
    val tokenEndpoint: String,
    val userInfoEndpoint: String,
    val jwksUri: String,
    val grantTypeSupported: Set<String>,
    val responseTypeSupported: Set<String>,
    val scopeSupported: Set<String>
)