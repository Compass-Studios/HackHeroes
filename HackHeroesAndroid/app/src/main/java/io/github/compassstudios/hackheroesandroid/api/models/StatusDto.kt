package io.github.compassstudios.hackheroesandroid.api.models

import kotlinx.serialization.Serializable

@Serializable
data class StatusDto(
    val environmentName: String,
    val applicationName: String,
    val isValidApiKey: Boolean,
)
