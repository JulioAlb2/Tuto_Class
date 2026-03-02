package com.example.tutoclass.feature.users.presentation.student

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import com.example.compose.*
import com.example.tutoclass.core.ui.theme.TutoBgCanvas
import com.example.tutoclass.feature.groups.presentation.components.GroupCard

@Composable
fun StudentHomeScreen(
    onLogout: () -> Unit,
    onNavigateToGroups: () -> Unit,
    onNavigateToGroup: (Int) -> Unit,
    viewModel: StudentHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showJoinDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showJoinDialog = true },
                containerColor = primaryLight,
                contentColor = onPrimaryLight
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
                        Icon(Icons.Default.School, contentDescription = null, tint = primaryLight, modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("TutoClass", color = primaryLight, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = state.user?.nombre ?: "Usuario",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurfaceLight
                            )
                            Text(
                                text = "🎓 ${state.user?.rol?.replaceFirstChar { it.uppercase() } ?: "Estudiante"}",
                                fontSize = 11.sp,
                                color = onSurfaceVariantLight
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = onLogout,
                            modifier = Modifier.size(36.dp).background(errorContainerLight, CircleShape)
                        ) {
                            Icon(Icons.Default.Logout, contentDescription = null, tint = errorLight, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "¡Hola, ${state.user?.nombre?.split(" ")?.firstOrNull() ?: ""}!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = onSurfaceLight
                )
                Text("Continúa tu camino de aprendizaje", color = onSurfaceVariantLight)

                Spacer(modifier = Modifier.height(24.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard(state.groups.size.toString(), "Clases Activas", Icons.Default.MenuBook, Modifier.weight(1f))
                    StatCard("24", "Horas Totales", Icons.Default.AccessTime, Modifier.weight(1f))
                    StatCard("1", "Tareas", Icons.Default.EmojiEvents, Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(28.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Tus Clases", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = onSurfaceLight)
                    TextButton(onClick = onNavigateToGroups) {
                        Text("Ver todas", color = primaryLight)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                } else if (state.error != null) {
                    Text("Error: ${state.error}", color = errorLight, modifier = Modifier.padding(8.dp))
                } else if (state.groups.isEmpty()) {
                    Text("No estás inscrito en ninguna clase aún.", color = onSurfaceVariantLight, modifier = Modifier.padding(8.dp))
                } else {
                    state.groups.take(3).forEach { group ->
                        GroupCard(
                            group = group,
                            onClick = { onNavigateToGroup(group.id) }
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
                        Text("Aceptar", color = primaryLight)
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
                Text("Ingresa el código que te proporcionó tu profesor.", fontSize = 14.sp, color = onSurfaceVariantLight)
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = code,
                    onValueChange = { if (it.length <= 6) code = it.uppercase() },
                    label = { Text("Código de acceso") },
                    placeholder = { Text("Ej: ABC123") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = primaryLight,
                        focusedLabelColor = primaryLight
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(code) },
                enabled = code.length >= 4,
                colors = ButtonDefaults.buttonColors(containerColor = primaryLight)
            ) {
                Text("Unirse")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = onSurfaceVariantLight)
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
            Icon(icon, null, tint = primaryLight, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = onSurfaceLight)
            Text(label, fontSize = 11.sp, color = onSurfaceVariantLight)
        }
    }
}
