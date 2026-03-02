package com.example.tutoclass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tutoclass.feature.users.presentation.navigation.NavGraph
import com.example.tutoclass.core.ui.theme.TutoClassTheme
import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authLocalDataSource: AuthLocalDataSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TutoClassTheme {
                NavGraph(authLocalDataSource = authLocalDataSource)
            }
        }
    }
}
