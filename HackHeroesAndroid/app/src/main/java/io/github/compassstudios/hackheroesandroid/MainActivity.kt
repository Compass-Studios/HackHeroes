package io.github.compassstudios.hackheroesandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import io.github.compassstudios.hackheroesandroid.ui.TranslationScreen
import io.github.compassstudios.hackheroesandroid.ui.theme.HackHeroesAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HackHeroesAndroidTheme {
                TranslationScreen(modifier = Modifier.fillMaxSize())
            }
        }
    }
}
