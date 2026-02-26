package com.example.tutoclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tutoclass.presentation.auth.LoginScreen // Asegúrate de que la ruta sea correcta
import com.example.tutoclass.ui.theme.TutoClassTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint // Requerido por Hilt para inyectar dependencias en esta actividad
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Habilita el diseño de borde a borde (transparencia en barras de estado)
        enableEdgeToEdge()

        setContent {
            TutoClassTheme {
                // Llamamos directamente a la pantalla de Login que creamos
                LoginScreen()
            }
        }
    }
}