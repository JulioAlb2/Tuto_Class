package com.example.tutoclass.feature.users.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tutoclass.feature.users.presentation.auth.LoginScreen
import com.example.tutoclass.feature.users.presentation.auth.RegisterScreen
import com.example.tutoclass.feature.users.presentation.student.StudentHomeScreen // Asegúrate de crear este archivo

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate("register")
                },
                onLoginSuccess = { role ->
                    if (role == "Estudiante") {
                        navController.navigate("student_home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                    // Aquí podrías añadir el if para el Tutor después
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        composable("student_home") {
            StudentHomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("student_home") { inclusive = true }
                    }
                }
            )
        }
    }
}