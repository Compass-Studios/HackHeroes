package io.github.compassstudios.hackheroesandroid.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import io.github.compassstudios.hackheroesandroid.api.models.StatusDto
import io.github.compassstudios.hackheroesandroid.api.models.TranslationDto

interface TranslationApi {
    @GET("status")
    suspend fun status(): StatusDto

    @POST("translate")
    @Headers("Content-Type: application/json")
    suspend fun translate(
        @Body request: TranslationDto.Request,
        @Header("Authorization") authorization: String,
    ): TranslationDto.Response
}
