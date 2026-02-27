package com.example.tutoclass.presentation.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutoclass.ui.theme.*
import com.example.tutoclass.presentation.components.*

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TutoBgCanvas)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Botón volver atrás
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToLogin) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TutoTextDark)
            }
            Text("Crear cuenta", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Bienvenido a TutoClass", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Regístrate para comenzar a aprender", color = TutoGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(20.dp))

                TutoTextField(
                    label = "Nombre completo",
                    value = uiState.name,
                    onValueChange = { viewModel.onNameChanged(it) },
                    icon = Icons.Default.Person
                )

                TutoTextField(
                    label = "Correo Electrónico",
                    value = uiState.email,
                    onValueChange = { viewModel.onEmailChanged(it) },
                    icon = Icons.Default.Email
                )

                TutoTextField(
                    label = "Contraseña",
                    value = uiState.password,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    icon = Icons.Default.Lock,
                    isPassword = true
                )

                Spacer(modifier = Modifier.height(30.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    TutoGradientButton(
                        text = "Registrarme",
                        onClick = { viewModel.signUp() }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    Modifier.fillMaxWidth().clickable { onBackToLogin() },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("¿Ya tienes cuenta? ")
                    Text("Inicia sesión", color = TutoGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}