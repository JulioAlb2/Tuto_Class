package com.example.tutoclass.feature.groups.presentation.teacher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.*
import com.example.tutoclass.core.ui.theme.*
import com.example.tutoclass.feature.groups.presentation.components.GroupCard
import com.example.tutoclass.feature.groups.presentation.components.GroupHeader

@Composable
fun TeacherHomeScreen(
    onLogout: () -> Unit,
    viewModel: TeacherHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var showCreateDialog by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showCreateDialog = true },
                containerColor = primaryLight,
                contentColor = onPrimaryLight
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear Clase")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(TutoBgCanvas)
        ) {
            // Header dinámico
            GroupHeader(
                role = "Maestro",
                onLogout = onLogout
            )

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "¡Hola, ${state.teacherName.split(" ").firstOrNull() ?: ""}!",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = onSurfaceLight
                )
                Text(
                    text = "Gestiona tus grupos y códigos de acceso",
                    color = onSurfaceVariantLight,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (state.isLoading) {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = primaryLight)
                    }
                }

                if (state.error != null) {
                    Text(text = "Error: ${state.error}", color = errorLight, modifier = Modifier.padding(8.dp))
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.groups) { group ->
                        GroupCard(
                            group = group,
                            showAccessCode = true
                        )
                    }
                }
            }
        }

        if (showCreateDialog) {
            CreateGroupDialog(
                onDismiss = { showCreateDialog = false },
                onConfirm = { name, subject, desc ->
                    viewModel.createGroup(name, subject, desc)
                    showCreateDialog = false
                }
            )
        }

        state.lastCreatedGroup?.let { group ->
            SuccessDialog(
                groupName = group.name,
                accessCode = group.accessCode,
                onDismiss = { viewModel.clearLastCreatedGroup() }
            )
        }
    }
}

@Composable
fun CreateGroupDialog(onDismiss: () -> Unit, onConfirm: (String, String, String) -> Unit) {
    var name by remember { mutableStateOf("") }
    var subject by remember { mutableStateOf("") }
    var desc by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Clase", fontWeight = FontWeight.Bold) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name, 
                    onValueChange = { name = it }, 
                    label = { Text("Nombre de la clase") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = subject, 
                    onValueChange = { subject = it }, 
                    label = { Text("Materia") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = desc, 
                    onValueChange = { desc = it }, 
                    label = { Text("Descripción (opcional)") },
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(name, subject, desc) }, 
                colors = ButtonDefaults.buttonColors(containerColor = primaryLight),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Crear", color = onPrimaryLight)
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
fun SuccessDialog(groupName: String, accessCode: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = primaryLight, modifier = Modifier.size(48.dp)) },
        title = { Text("¡Clase Creada!", fontWeight = FontWeight.Bold) },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text(text = "Clase: $groupName", fontWeight = FontWeight.Medium)
                Text(text = "Comparte este código con tus alumnos:", fontSize = 14.sp, color = onSurfaceVariantLight)
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = surfaceContainerLight,
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, primaryLight)
                ) {
                    Text(
                        accessCode,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp,
                        color = primaryLight
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss, 
                colors = ButtonDefaults.buttonColors(containerColor = primaryLight),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Entendido", color = onPrimaryLight)
            }
        }
    )
}
