package com.example.tutoclass.feature.users.presentation.auth

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
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
import com.example.compose.backgroundLight
import com.example.compose.primaryDark
import com.example.tutoclass.core.ui.theme.TutoBgCanvas
import com.example.tutoclass.core.ui.theme.TutoGray
import com.example.tutoclass.feature.users.presentation.components.TutoGradientButton
import com.example.tutoclass.feature.users.presentation.components.TutoTextField

@Composable
fun RegisterScreen(
    onBackToLogin: () -> Unit,
    onRegisterSuccess: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundLight)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackToLogin) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = primaryDark)
            }
            Text("Crear cuenta", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Bienvenido a TutoClass", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Text("Regístrate para comenzar a aprender", color = TutoGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(20.dp))

                // Selección de Rol
                Text("¿Quién eres?", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = uiState.role == "Estudiante",
                        onClick = { viewModel.onRoleChanged("Estudiante") },
                        label = { Text("Soy Estudiante") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = uiState.role == "Maestro",
                        onClick = { viewModel.onRoleChanged("Maestro") },
                        label = { Text("Soy Maestro") },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

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

                // Campo extra para maestros
                if (uiState.role == "Maestro") {
                    TutoTextField(
                        label = "Materias (separadas por coma)",
                        value = uiState.subjects,
                        onValueChange = { viewModel.onSubjectsChanged(it) },
                        icon = Icons.AutoMirrored.Filled.MenuBook
                    )
                }

                if (uiState.error != null) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    TutoGradientButton(
                        text = "Registrarme",
                        onClick = { viewModel.signUp(onSuccess = onRegisterSuccess) }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onBackToLogin() },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text("¿Ya tienes cuenta? ")
                    Text("Inicia sesión", color = primaryDark, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}