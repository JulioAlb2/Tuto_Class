package com.example.tutoclass.feature.users.presentation.student

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tutoclass.core.ui.theme.TutoBgCanvas
import com.example.tutoclass.core.ui.theme.TutoGradient
import com.example.tutoclass.core.ui.theme.TutoGray
import com.example.tutoclass.core.ui.theme.TutoGreen
import com.example.tutoclass.core.ui.theme.TutoTextDark
import com.example.tutoclass.ui.theme.*

@Composable
fun StudentHomeScreen(
    onLogout: () -> Unit,
    viewModel: StudentHomeViewModel = hiltViewModel() // Inyectamos el ViewModel
) {
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(TutoBgCanvas)
            .verticalScroll(rememberScrollState())
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = null, tint = TutoGreen, modifier = Modifier.size(28.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("TutoClass", color = TutoGreen, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(horizontalAlignment = Alignment.End) {
                        Text("Rocio Ballinas", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                        Text("🎓 Estudiante", fontSize = 11.sp, color = TutoGray)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    IconButton(
                        onClick = onLogout,
                        modifier = Modifier.size(36.dp).background(Color(0xFFFFEBEE), CircleShape)
                    ) {
                        Icon(Icons.Default.Logout, contentDescription = null, tint = Color.Red, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }

        Column(modifier = Modifier.padding(20.dp)) {
            Text("¡Hola, Rocio Ballinas!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TutoTextDark)
            Text("Continúa tu camino de aprendizaje", color = TutoGray)

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("3", "Clases Activas", Icons.Default.MenuBook, Modifier.weight(1f))
                StatCard("24", "Horas Totales", Icons.Default.AccessTime, Modifier.weight(1f))
                StatCard("1", "Tareas", Icons.Default.EmojiEvents, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text("Próximas Clases", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TutoTextDark)
            Spacer(modifier = Modifier.height(12.dp))
            NextClassCard("Moviles 1", "Ali López Zúnun", "Hoy, 3:00 PM", "En 2 horas")

            Spacer(modifier = Modifier.height(28.dp))

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Buscar clases, tutores o materias...", fontSize = 14.sp) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TutoGray) },
                colors = OutlinedTextFieldDefaults.colors(focusedContainerColor = Color.White, unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))

            val categories = listOf("Todas", "Matemáticas", "Idiomas", "Ciencias", "Tecnología")
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(categories) { cat ->
                    val isSelected = cat == "Todas"
                    FilterChip(
                        selected = isSelected,
                        onClick = {},
                        label = { Text(cat) },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = TutoGreen,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text("Explorar Clases", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TutoTextDark)
            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = TutoGreen)
                }
            } else if (state.error != null) {
                Text("Error al cargar clases", color = Color.Red, modifier = Modifier.padding(8.dp))
            } else {
                state.groups.forEach { group ->
                    CourseCard(
                        title = group.nombre,
                        teacher = "Prof. ${group.nombreProfesor}",
                        emoji = if (group.materia.contains("Ingles", true)) "🗣️" else "📐"
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun StatCard(value: String, label: String, icon: ImageVector, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Icon(icon, null, tint = TutoGreen, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TutoTextDark)
            Text(label, fontSize = 11.sp, color = TutoGray)
        }
    }
}

@Composable
fun NextClassCard(title: String, teacher: String, time: String, countdown: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(modifier = Modifier.background(TutoGradient).padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text(title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(teacher, color = Color.White.copy(alpha = 0.9f), fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(time, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
                }
                Surface(color = Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp)) {
                    Text(countdown, color = Color.White, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp), fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun CourseCard(title: String, teacher: String, emoji: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(60.dp).background(TutoBgCanvas, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(emoji, fontSize = 30.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(teacher, color = TutoGray, fontSize = 13.sp)
            }
            Button(
                onClick = {},
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TutoGreen)
            ) {
                Text("Ver Clase", fontSize = 12.sp)
            }
        }
    }
}