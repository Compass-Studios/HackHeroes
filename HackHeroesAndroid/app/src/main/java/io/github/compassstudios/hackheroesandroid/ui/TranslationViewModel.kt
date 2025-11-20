package io.github.compassstudios.hackheroesandroid.ui

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.lifecycle.viewModelScope
import io.github.compassstudios.hackheroesandroid.api.ApiClient
import io.github.compassstudios.hackheroesandroid.api.models.TranslationDto
import io.github.compassstudios.hackheroesandroid.datastore.credentialsDataStore
import io.github.compassstudios.hackheroesandroid.helpers.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslationViewModel(application: Application) : AndroidViewModel(application) {
    var input by mutableStateOf("")

    private val _output = MutableStateFlow<LoadingState<String>?>(null)
    val output = _output.asStateFlow()

    var areSettingsVisible by mutableStateOf(false)
    val canSettingsBeDismissed = application.credentialsDataStore.data.map { it != null }

    fun translate() = viewModelScope.launch {
        _output.update { LoadingState.Loading }
        try {
            val credentials = application.credentialsDataStore.data.first()

            val res = ApiClient.translationApi.translate(
                request = TranslationDto.Request(
                    message = input,
                    direction = TranslationDto.Direction.FromBrainrot,
                ),
                basePath = credentials!!.apiUrl,
                authorization = "Bearer ${credentials.apiKey}",
            )
            _output.update { LoadingState.Success(res.message) }
        } catch (e: Exception) {
            Log.e("TranslationViewModel", null, e)
            _output.update { LoadingState.Error(e) }
        }
    }
}
