package io.github.compassstudios.hackheroesandroid.api.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

object TranslationDto {
    @Serializable
    data class Request(
        val message: String,
        val direction: Direction,
    )

    @Serializable
    enum class Direction {
        @SerialName("genx-to-genz") ToBrainrot,
        @SerialName("genz-to-genx") FromBrainrot,
    }

    @Serializable
    data class Response(
        val message: String,
    )
}
