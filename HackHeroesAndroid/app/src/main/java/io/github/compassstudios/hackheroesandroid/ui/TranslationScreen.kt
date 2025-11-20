package io.github.compassstudios.hackheroesandroid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.compassstudios.hackheroesandroid.R
import io.github.compassstudios.hackheroesandroid.helpers.LoadingState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    modifier: Modifier = Modifier,
    viewModel: TranslationViewModel = viewModel()
) {
    val output by viewModel.output.collectAsState()

    if (viewModel.areSettingsVisible) {
        SettingsDialog(
            onDismissRequest = { viewModel.areSettingsVisible = false },
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(stringResource(R.string.app_name))
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.areSettingsVisible = true },
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.settings_title),
                        )
                    }
                },
            )
        },
        modifier = modifier
    ) { insets ->
        Column(
            modifier = Modifier.padding(insets)
        ) {
            InputInterface(
                value = viewModel.input,
                onValueChange = { viewModel.input = it },
                onTranslate = { viewModel.translate() },
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp)
            )

            output?.let {
                OutputCard(
                    isLoading = it is LoadingState.Loading,
                    text = when (it) {
                        is LoadingState.Success -> it.data
                        is LoadingState.Error<*> -> it.error.message!!
                        else -> ""
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun InputInterface(
    value: String,
    onValueChange: (String) -> Unit,
    onTranslate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(stringResource(R.string.translator_input_placeholder))
            },
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedButton(
                onClick = { onValueChange("") },
            ) {
                Text(stringResource(R.string.translator_clear_button))
            }
            Button(
                onClick = onTranslate,
            ) {
                Text(stringResource(R.string.translator_translate_button))
            }
        }
    }
}

@Composable
private fun OutputCard(
    isLoading: Boolean,
    text: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
    ) {
        if (isLoading) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                CircularProgressIndicator()
            }
        } else {
            Text(
                text = text,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Preview
@Composable
private fun TranslationScreenPreview() {
    Surface {
        TranslationScreen(modifier = Modifier.fillMaxSize())
    }
}
