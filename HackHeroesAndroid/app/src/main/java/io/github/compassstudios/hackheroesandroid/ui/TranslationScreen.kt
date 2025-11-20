package io.github.compassstudios.hackheroesandroid.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import io.github.compassstudios.hackheroesandroid.R
import io.github.compassstudios.hackheroesandroid.api.models.TranslationDto.Direction
import io.github.compassstudios.hackheroesandroid.helpers.LoadingState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    modifier: Modifier = Modifier,
    viewModel: TranslationViewModel = viewModel()
) {
    val output by viewModel.output.collectAsState()
    val canSettingsBeDismissed by viewModel.canSettingsBeDismissed.collectAsState(true)

    if (viewModel.areSettingsVisible || !canSettingsBeDismissed) {
        SettingsDialog(
            onDismissRequest = {
                viewModel.areSettingsVisible = false
            },
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
                direction = viewModel.direction,
                onDirectionChange = {
                    viewModel.direction = when (viewModel.direction) {
                        Direction.ToBrainrot -> Direction.FromBrainrot
                        Direction.FromBrainrot -> Direction.ToBrainrot
                    }
                },
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
fun LanguageCard(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        contentAlignment = Alignment.Center,
        content = content,
        modifier = Modifier
            .width(120.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(horizontal = 10.dp)
            .padding(vertical = 8.dp)
            .then(modifier)
    )
}

@Composable
private fun InputInterface(
    value: String,
    onValueChange: (String) -> Unit,
    direction: Direction,
    onDirectionChange: () -> Unit,
    onTranslate: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            LanguageCard {
                Text(if (direction == Direction.ToBrainrot) {
                    stringResource(R.string.language_normal)
                } else {
                    stringResource(R.string.language_brainrot)
                })
            }
            IconButton(
                onClick = onDirectionChange,
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                    contentDescription = stringResource(R.string.translator_switch_direction_button),
                )
            }
            LanguageCard {
                Text(if (direction == Direction.FromBrainrot) {
                    stringResource(R.string.language_normal)
                } else {
                    stringResource(R.string.language_brainrot)
                })
            }
        }

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(stringResource(R.string.translator_input_placeholder))
            },
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth()
                .padding(vertical = 8.dp)
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

@Preview
@Composable
private fun InputInterfacePreview() {
    Surface {
        InputInterface(
            value = "",
            onValueChange = {},
            onTranslate = {},
            direction = Direction.ToBrainrot,
            onDirectionChange = {},
        )
    }
}

@Preview
@Composable
private fun OutputCardPreview() {
    Surface {
        OutputCard(
            isLoading = false,
            text = "Brainrot",
        )
    }
}
