package io.github.compassstudios.hackheroesandroid.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.compassstudios.hackheroesandroid.api.ApiClient
import io.github.compassstudios.hackheroesandroid.api.models.TranslationDto
import io.github.compassstudios.hackheroesandroid.helpers.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslationViewModel : ViewModel() {
    var input by mutableStateOf("")

    private val _output = MutableStateFlow<LoadingState<String>?>(null)
    val output = _output.asStateFlow()

    var areSettingsVisible by mutableStateOf(false)

    fun translate() = viewModelScope.launch {
        _output.update { LoadingState.Loading }
        try {
            val res = ApiClient.translationApi.translate(
                request = TranslationDto.Request(
                    message = input,
                    direction = TranslationDto.Direction.FromBrainrot,
                ),
                // TODO: Unharcode
                authorization = "Bearer stoic-faraday",
            )
            _output.update { LoadingState.Success(res.message) }
        } catch (e: Exception) {
            Log.e("TranslationViewModel", "Error translating", e)
            _output.update { LoadingState.Error(e) }
        }
    }
}
