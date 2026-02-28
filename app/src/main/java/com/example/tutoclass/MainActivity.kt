package com.example.tutoclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tutoclass.feature.users.presentation.navigation.NavGraph
import com.example.tutoclass.core.ui.theme.TutoClassTheme
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