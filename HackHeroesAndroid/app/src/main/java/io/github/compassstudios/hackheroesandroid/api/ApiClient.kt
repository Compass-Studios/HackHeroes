package io.github.compassstudios.hackheroesandroid.api

import de.jensklingenberg.ktorfit.Ktorfit
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object ApiClient {
    private val ktorfit = Ktorfit.Builder()
        .httpClient {
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = 60_000
                requestTimeoutMillis = 30_000
            }
        }
        .build()

    val translationApi = ktorfit.createTranslationApi()
}
