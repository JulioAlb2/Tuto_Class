package com.example.tutoclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tutoclass.presentation.navigation.NavGraph // Importamos el mapa de navegación
import com.example.tutoclass.ui.theme.TutoClassTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TutoClassTheme {

                NavGraph()
            }
        }
    }
}