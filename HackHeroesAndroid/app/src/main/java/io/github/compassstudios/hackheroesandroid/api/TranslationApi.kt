package io.github.compassstudios.hackheroesandroid.api

import de.jensklingenberg.ktorfit.http.Body
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Header
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.POST
import de.jensklingenberg.ktorfit.http.Path
import io.github.compassstudios.hackheroesandroid.api.models.StatusDto
import io.github.compassstudios.hackheroesandroid.api.models.TranslationDto

interface TranslationApi {
    @GET("{basePath}/status")
    suspend fun status(
        @Path basePath: String,
        @Header("Authorization") authorization: String,
    ): StatusDto

    @POST("{basePath}/translate")
    @Headers("Content-Type: application/json")
    suspend fun translate(
        @Path basePath: String,
        @Body request: TranslationDto.Request,
        @Header("Authorization") authorization: String,
    ): TranslationDto.Response
}
