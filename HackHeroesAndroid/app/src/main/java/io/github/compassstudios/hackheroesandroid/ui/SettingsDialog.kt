package io.github.compassstudios.hackheroesandroid.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.compassstudios.hackheroesandroid.R
import io.github.compassstudios.hackheroesandroid.helpers.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = viewModel(),
) {
    val serverStatus by viewModel.serverStatus.collectAsState()
    if (serverStatus is LoadingState.Success) onDismissRequest()

    LaunchedEffect(null) {
        viewModel.restoreState()
    }

    AlertDialog(
        title = {
            Text(stringResource(R.string.settings_title))
        },
        confirmButton = {
            TextButton(
                onClick = {
                    viewModel.saveSettings()
                },
            ) {
                if (serverStatus is LoadingState.Loading) {
                    CircularProgressIndicator()
                }
                Text(stringResource(R.string.save_button))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                },
            ) {
                Text(stringResource(R.string.cancel_button))
            }
        },
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OutlinedTextField(
                    value = viewModel.apiUrl,
                    onValueChange = { viewModel.apiUrl = it },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri),
                    supportingText = {
                        serverStatus?.let {
                            if (it is LoadingState.Error<*> && it.error is SettingsViewModel.InvalidUrlError) {
                                Text(
                                    text = stringResource(R.string.settings_error_invalid_url),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    },
                    label = {
                        Text(stringResource(R.string.settings_api_url))
                    },
                )

                OutlinedTextField(
                    value = viewModel.apiKey,
                    onValueChange = { viewModel.apiKey = it },
                    supportingText = {
                        serverStatus?.let {
                            if (it is LoadingState.Error<*> && it.error is SettingsViewModel.InvalidKeyError) {
                                Text(
                                    text = stringResource(R.string.settings_error_invalid_key),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        }
                    },
                    label = {
                        Text(stringResource(R.string.settings_api_key))
                    }
                )

                serverStatus?.let {
                    if (
                        it is LoadingState.Error<*>
                        && it.error !is SettingsViewModel.InvalidUrlError
                        && it.error !is SettingsViewModel.InvalidKeyError
                    ) {
                        Text(
                            text = it.error.localizedMessage ?: it.error.toString(),
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
            }
        },
    )
}

@Preview
@Composable
private fun SettingsDialogPreview() {
    SettingsDialog(
        onDismissRequest = {},
    )
}
