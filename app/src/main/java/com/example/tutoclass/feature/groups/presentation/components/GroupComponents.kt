package com.example.tutoclass.feature.groups.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.compose.*
import com.example.tutoclass.core.ui.theme.TutoBgCanvas
import com.example.tutoclass.feature.groups.domain.model.Group

@Composable
fun GroupHeader(
    role: String,
    userName: String = "Usuario",
    onLogout: () -> Unit
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
                    Text(userName, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = onSurfaceLight)
                    Text(if (role.contains("maestro", true)) "👨‍🏫 Profesor" else "🎓 Estudiante", fontSize = 11.sp, color = onSurfaceVariantLight)
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
}

@Composable
fun GroupDetailInfo(
    group: Group,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(primaryContainerLight, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Class, null, tint = primaryLight)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = group.subject, fontSize = 12.sp, color = primaryLight, fontWeight = FontWeight.Bold)
                    Text(text = group.name, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = onSurfaceLight)
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
            Divider(color = outlineVariantLight.copy(alpha = 0.5f))
            Spacer(modifier = Modifier.height(20.dp))

            DetailItem(icon = Icons.Default.Person, label = "Profesor", value = group.teacherName)
            Spacer(modifier = Modifier.height(16.dp))
            DetailItem(icon = Icons.Default.Info, label = "Descripción", value = group.description ?: "Sin descripción disponible")
            Spacer(modifier = Modifier.height(16.dp))
            DetailItem(icon = Icons.Default.Key, label = "Código de acceso", value = group.accessCode)
        }
    }
}

@Composable
private fun DetailItem(icon: ImageVector, label: String, value: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = primaryLight, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = onSurfaceVariantLight, fontWeight = FontWeight.Medium)
            Text(text = value, fontSize = 14.sp, color = onSurfaceLight, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
fun GroupCard(
    group: Group,
    showAccessCode: Boolean = false,
    onClick: () -> Unit = {}
) {
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
                Icon(Icons.Default.Groups, contentDescription = null, tint = primaryLight)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(group.name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = onSurfaceLight)
                Text(if (showAccessCode) group.subject else "Prof. ${group.teacherName}", color = onSurfaceVariantLight, fontSize = 13.sp)
            }
            
            if (showAccessCode) {
                Column(horizontalAlignment = Alignment.End) {
                    Text("CÓDIGO", fontSize = 10.sp, color = onSurfaceVariantLight, fontWeight = FontWeight.Bold)
                    Text(
                        group.accessCode,
                        color = primaryLight,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                }
            } else {
                Button(
                    onClick = { onClick() },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryLight)
                ) {
                    Text("Ver Clase", fontSize = 12.sp, color = onPrimaryLight)
                }
            }
        }
    }
}
