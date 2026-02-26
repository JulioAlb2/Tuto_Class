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
// Importamos los colores y los componentes de las otras carpetas
import com.example.tutoclass.ui.theme.*
import com.example.tutoclass.presentation.components.*

@Composable
fun LoginScreen() {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Estudiante") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TutoBgCanvas)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Logo
        Box(
            modifier = Modifier
                .size(80.dp)
                .background(TutoGradient, RoundedCornerShape(20.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.School, contentDescription = null, tint = Color.White, modifier = Modifier.size(45.dp))
        }
        Text("TutoClass", fontSize = 34.sp, fontWeight = FontWeight.Bold, color = TutoTextDark)
        Text("Aprende sin límites", color = TutoGray)

        Spacer(modifier = Modifier.height(30.dp))

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Iniciar Sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Accede a tu cuenta", color = TutoGray, fontSize = 14.sp)

                Spacer(modifier = Modifier.height(16.dp))

                // Estos ahora se leen de TutoComponents.kt
                TutoTextField("Correo Electrónico", email, { email = it }, Icons.Default.Email)
                TutoTextField("Contraseña", password, { password = it }, Icons.Default.Lock, true)

                Text("Ingresar como", modifier = Modifier.padding(top = 12.dp, bottom = 8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    RoleButton("Estudiante", selectedRole == "Estudiante", { selectedRole = "Estudiante" }, Modifier.weight(1f), "🎓")
                    RoleButton("Tutor", selectedRole == "Tutor", { selectedRole = "Tutor" }, Modifier.weight(1f), "👨‍🏫")
                }

                Spacer(modifier = Modifier.height(24.dp))

                TutoGradientButton(text = "Entrar", onClick = { /* TODO: Login */ })

                Spacer(modifier = Modifier.height(16.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                    Text("¿No tienes cuenta? ")
                    Text("Regístrate gratis", color = TutoGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}