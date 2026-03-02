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
import com.example.tutoclass.core.ui.theme.*

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
                containerColor = TutoGreen,
                contentColor = Color.White
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
            // Header (Similar al del estudiante pero con rol Maestro)
            TeacherHeader(onLogout)

            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Tus Clases",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = TutoTextDark
                )
                Text("Gestiona tus grupos y códigos de acceso", color = TutoGray)

                Spacer(modifier = Modifier.height(20.dp))

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally), color = TutoGreen)
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(state.groups) { group ->
                        TeacherGroupCard(group)
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
fun TeacherGroupCard(group: com.example.tutoclass.feature.groups.domain.model.Group) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(50.dp).background(TutoBgCanvas, RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Groups, contentDescription = null, tint = TutoGreen)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(group.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(group.subject, color = TutoGray, fontSize = 13.sp)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("CÓDIGO", fontSize = 10.sp, color = TutoGray, fontWeight = FontWeight.Bold)
                Text(
                    group.accessCode,
                    color = TutoGreen,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
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
        title = { Text("Nueva Clase") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre de la clase") })
                OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Materia") })
                OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Descripción (opcional)") })
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(name, subject, desc) }, colors = ButtonDefaults.buttonColors(containerColor = TutoGreen)) {
                Text("Crear")
            }
        }
    )
}

@Composable
fun SuccessDialog(groupName: String, accessCode: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TutoGreen, modifier = Modifier.size(48.dp)) },
        title = { Text("¡Clase Creada!") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("Comparte este código con tus alumnos:")
                Spacer(modifier = Modifier.height(16.dp))
                Surface(
                    color = TutoBgCanvas,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, TutoGreen)
                ) {
                    Text(
                        accessCode,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp,
                        color = TutoGreen
                    )
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss, colors = ButtonDefaults.buttonColors(containerColor = TutoGreen)) {
                Text("Entendido")
            }
        }
    )
}

@Composable
fun TeacherHeader(onLogout: () -> Unit) {
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
                    Text("Panel Maestro", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                    Text("👨‍🏫 Profesor", fontSize = 11.sp, color = TutoGray)
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
}
