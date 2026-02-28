package com.example.tutoclass.feature.users.presentation.auth

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
import com.example.tutoclass.core.ui.theme.TutoBgCanvas
import com.example.tutoclass.core.ui.theme.TutoGradient
import com.example.tutoclass.core.ui.theme.TutoGray
import com.example.tutoclass.core.ui.theme.TutoGreen
import com.example.tutoclass.core.ui.theme.TutoTextDark
import com.example.tutoclass.feature.users.presentation.components.TutoGradientButton
import com.example.tutoclass.feature.users.presentation.components.TutoTextField

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TutoBgCanvas)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Box(
            modifier = Modifier
                .size(80.dp)
                .background(TutoGradient, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.School, null, tint = Color.White, modifier = Modifier.size(45.dp))
        }
        Text("TutoClass", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = TutoTextDark)
        Text("Aprende sin límites", color = TutoGray)

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Iniciar Sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Accede a tu cuenta", color = TutoGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))

                TutoTextField(
                    label = "Correo Electrónico",
                    value = uiState.email,
                    onValueChange = { email -> viewModel.onLoginChanged(email, uiState.password) },
                    icon = Icons.Default.Email
                )

                TutoTextField(
                    label = "Contraseña",
                    value = uiState.password,
                    onValueChange = { pass -> viewModel.onLoginChanged(uiState.email, pass) },
                    icon = Icons.Default.Lock,
                    isPassword = true
                )

                if (uiState.error != null) {
                    Text(
                        text = uiState.error ?: "",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(top = 8.dp),
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (uiState.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    TutoGradientButton(
                        text = "Entrar",
                        onClick = {
                            viewModel.login(onSuccess = onLoginSuccess)
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("¿No tienes cuenta? ")
                    Text(
                        text = "Regístrate gratis",
                        color = TutoGreen,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToRegister() }
                    )
                }
            }
        }
    }
}