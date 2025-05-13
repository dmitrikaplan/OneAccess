package ru.kaplaan.kcloak.web.mapper

import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import ru.kaplaan.kcloak.config.properties.OneAccessClientSettings
import ru.kaplaan.kcloak.config.properties.OneAccessTokenSettings


fun OneAccessClientSettings.toClientSettings(): ClientSettings {
    return ClientSettings.builder()
        .requireProofKey(requireProofKey)
        .requireAuthorizationConsent(requireAuthorizationConsent)
        .build()
}

fun ClientSettings.toOneAccessClientSettings(): OneAccessClientSettings {
    return OneAccessClientSettings(
        requireAuthorizationConsent = this.isRequireAuthorizationConsent,
        requireProofKey = this.isRequireProofKey
    )
}


fun OneAccessTokenSettings.toTokenSettings(): TokenSettings {
    return TokenSettings
        .builder()
        .accessTokenTimeToLive(accessTokenLifespan)
        .refreshTokenTimeToLive(refreshTokenLifespan)
        .authorizationCodeTimeToLive(authorizationCodeLifespan)
        .reuseRefreshTokens(reuseRefreshTokens)
        .build()
}

fun TokenSettings.toOneAccessTokenSettings(): OneAccessTokenSettings {
    return OneAccessTokenSettings(
        accessTokenLifespan = accessTokenTimeToLive,
        refreshTokenLifespan = refreshTokenTimeToLive,
        authorizationCodeLifespan = authorizationCodeTimeToLive,
        reuseRefreshTokens = isReuseRefreshTokens
    )
}