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
import androidx.compose.runtime.*
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

@Composable
fun StudentHomeScreen(
    onLogout: () -> Unit,
    viewModel: StudentHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showJoinDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showJoinDialog = true },
                containerColor = TutoGreen,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Unirse a una clase")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
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
                            Text("Estudiante", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            Text("🎓 En línea", fontSize = 11.sp, color = TutoGray)
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
                Text("¡Hola!", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TutoTextDark)
                Text("Continúa tu camino de aprendizaje", color = TutoGray)

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(state.groups.size.toString(), "Clases Activas", Icons.Default.MenuBook, Modifier.weight(1f))
                    StatCard("24", "Horas Totales", Icons.Default.AccessTime, Modifier.weight(1f))
                    StatCard("1", "Tareas", Icons.Default.EmojiEvents, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(28.dp))

                Text("Tus Clases", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TutoTextDark)
                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = TutoGreen)
                    }
                } else if (state.error != null) {
                    Text("Error: ${state.error}", color = Color.Red, modifier = Modifier.padding(8.dp))
                } else if (state.groups.isEmpty()) {
                    Text("No estás inscrito en ninguna clase aún.", color = TutoGray, modifier = Modifier.padding(8.dp))
                } else {
                    state.groups.forEach { group ->
                        CourseCard(
                            title = group.name,
                            teacher = "Prof. ${group.teacherName}",
                            emoji = if (group.subject.contains("Ingles", true)) "🗣️" else "📐"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        if (showJoinDialog) {
            JoinGroupDialog(
                onDismiss = { showJoinDialog = false },
                onConfirm = { code ->
                    viewModel.joinGroup(code)
                    showJoinDialog = false
                }
            )
        }

        if (state.joinSuccess) {
            AlertDialog(
                onDismissRequest = { viewModel.resetJoinState() },
                title = { Text("¡Éxito!") },
                text = { Text("Te has unido a la clase correctamente.") },
                confirmButton = {
                    TextButton(onClick = { viewModel.resetJoinState() }) {
                        Text("Aceptar", color = TutoGreen)
                    }
                }
            )
        }
    }
}

@Composable
fun JoinGroupDialog(onDismiss: () -> Unit, onConfirm: (String) -> Unit) {
    var code by remember { mutableStateOf("") }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Unirse a una clase", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                Text("Ingresa el código que te proporcionó tu profesor.", fontSize = 14.sp, color = TutoGray)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = code,
                    onValueChange = { if (it.length <= 6) code = it.uppercase() },
                    label = { Text("Código de acceso") },
                    placeholder = { Text("Ej: ABC123") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(code) },
                enabled = code.length >= 4,
                colors = ButtonDefaults.buttonColors(containerColor = TutoGreen)
            ) {
                Text("Unirse")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TutoGray)
            }
        }
    )
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
