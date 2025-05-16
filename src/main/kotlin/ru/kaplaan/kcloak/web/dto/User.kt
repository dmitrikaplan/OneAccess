package ru.kaplaan.kcloak.web.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class User(
    val id: Long,
    private val username: String,
    val email: String,
    val enabled: Boolean,
    val firstName: String,
    val lastName: String,
    private val password: String,
    private val permissions: Set<String>,
    private val roles: Set<String>,
) : UserDetails {

    override fun getAuthorities(): List<GrantedAuthority> {
        return permissions.map {
            SimpleGrantedAuthority(it)
        } + roles.map {
            SimpleGrantedAuthority(it)
        }
    }

    override fun getPassword(): String {
        return password
    }

    override fun getUsername(): String {
        return username
    }

    override fun isEnabled(): Boolean {
        return enabled
    }
}