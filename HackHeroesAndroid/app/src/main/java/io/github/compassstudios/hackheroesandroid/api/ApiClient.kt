package io.github.compassstudios.hackheroesandroid.api

import de.jensklingenberg.ktorfit.Ktorfit
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
        }
        // TODO: Unhardcode
        .baseUrl("http://10.0.2.2:5014/")
        .build()

    val translationApi = ktorfit.createTranslationApi()
}
