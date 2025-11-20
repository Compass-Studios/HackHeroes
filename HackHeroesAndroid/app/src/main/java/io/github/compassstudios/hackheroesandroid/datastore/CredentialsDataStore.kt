package io.github.compassstudios.hackheroesandroid.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class Credentials(
    val apiUrl: String,
    val apiKey: String,
)

object CredentialsSerializer : Serializer<Credentials?> {
    override val defaultValue = null

    override suspend fun readFrom(input: InputStream): Credentials? =
        try {
            Json.decodeFromString<Credentials>(
                input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            throw CorruptionException("Unable to read Credentials", serialization)
        }

    override suspend fun writeTo(t: Credentials?, output: OutputStream) {
        output.write(
            Json.encodeToString(t)
                .encodeToByteArray()
        )
    }
}

val Context.credentialsDataStore: DataStore<Credentials?> by dataStore(
    fileName = "credentials.json",
    serializer = CredentialsSerializer,
)
