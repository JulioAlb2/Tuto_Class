package com.example.tutoclass.feature.users.presentation.navigation

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tutoclass.feature.users.presentation.student.StudentHomeScreen
import com.example.tutoclass.feature.groups.presentation.teacher.TeacherHomeScreen
import com.example.tutoclass.feature.users.data.datasource.local.AuthLocalDataSource
import com.example.tutoclass.feature.users.presentation.auth.LoginScreen
import com.example.tutoclass.feature.users.presentation.auth.RegisterScreen
import kotlinx.coroutines.flow.first

@Composable
fun NavGraph(
    authLocalDataSource: AuthLocalDataSource
) {
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
                    navController.navigate("check_role") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("check_role") {
            LaunchedEffect(Unit) {
                val user = authLocalDataSource.getUser().first()
                val role = user?.rol?.lowercase()?.trim() ?: ""
                
                Log.d("NAV_GRAPH", "Role detectado: '$role'")

                // Verificación flexible del rol
                if (role.contains("maestro") || role.contains("teacher") || role.contains("profesor")) {
                    Log.d("NAV_GRAPH", "Navegando a Teacher Home")
                    navController.navigate("teacher_home") {
                        popUpTo("check_role") { inclusive = true }
                    }
                } else {
                    // Por defecto si es alumno, estudiante o cualquier otro, va a Student Home
                    Log.d("NAV_GRAPH", "Navegando a Student Home")
                    navController.navigate("student_home") {
                        popUpTo("check_role") { inclusive = true }
                    }
                }
            }
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

        composable("teacher_home") {
            TeacherHomeScreen(
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("teacher_home") { inclusive = true }
                    }
                }
            )
        }
    }
}
