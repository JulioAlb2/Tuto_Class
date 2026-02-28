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
                onLoginSuccess = {
                    navController.navigate("student_home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("register") {
            RegisterScreen(
                onBackToLogin = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
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