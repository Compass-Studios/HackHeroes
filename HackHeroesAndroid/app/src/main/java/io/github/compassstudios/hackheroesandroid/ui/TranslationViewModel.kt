package io.github.compassstudios.hackheroesandroid.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.compassstudios.hackheroesandroid.helpers.LoadingState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TranslationViewModel : ViewModel() {
    var input by mutableStateOf("")

    private val _output = MutableStateFlow<LoadingState<String>?>(null)
    val output = _output.asStateFlow()

    fun translate() = viewModelScope.launch {
        _output.update { LoadingState.Loading }
        delay(2_000)
        _output.update { LoadingState.Success("Brainrot") }
    }
}
