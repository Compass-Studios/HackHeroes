package io.github.compassstudios.hackheroesandroid.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.compassstudios.hackheroesandroid.api.ApiClient
import io.github.compassstudios.hackheroesandroid.helpers.LoadingState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel : ViewModel() {
    var apiUrl by mutableStateOf("")
    var apiKey by mutableStateOf("")

    private val _serverStatus = MutableStateFlow<LoadingState<Unit>?>(null)
    val serverStatus = _serverStatus.asStateFlow()

    class InvalidUrlError : Error()

    fun saveSettings() = viewModelScope.launch {
        if (!apiUrl.startsWith("http://") && !apiUrl.startsWith("https://")) {
            _serverStatus.update { LoadingState.Error(InvalidUrlError()) }
            return@launch
        }

        if (!apiUrl.endsWith('/')) apiUrl += '/'
        _serverStatus.update { LoadingState.Loading }
        try {
            ApiClient.translationApi.status(apiUrl + "status")
            _serverStatus.update { LoadingState.Success(Unit) }
        } catch (e: Exception) {
            Log.e("SettingsViewModel", null, e)
            _serverStatus.update { LoadingState.Error(e) }
        }
    }
}
