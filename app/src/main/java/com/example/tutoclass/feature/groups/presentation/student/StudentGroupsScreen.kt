package com.example.tutoclass.feature.groups.presentation.student

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Class
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.*
import com.example.tutoclass.core.ui.theme.TutoBgCanvas
import com.example.tutoclass.feature.groups.presentation.components.StudentGroupItem
import com.example.tutoclass.feature.users.presentation.student.StudentHomeViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGroupsScreen(
    onBack: () -> Unit,
    onNavigateToGroup: (Int) -> Unit,
    viewModel: StudentHomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mis Clases", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(TutoBgCanvas)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = primaryLight
                )
            } else if (state.groups.isEmpty()) {
                EmptyGroupsView(modifier = Modifier.align(Alignment.Center))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
                    item {
                        Text(
                            text = "Tienes ${state.groups.size} clases activas",
                            fontSize = 14.sp,
                            color = onSurfaceVariantLight,
                            modifier = Modifier.padding(bottom = 16.dp, start = 4.dp)
                        )
                    }
                    items(state.groups) { group ->
                        StudentGroupItem(
                            name = group.name,
                            subject = group.subject,
                            teacherName = "Prof. ${group.teacherName}",
                            onClick = { onNavigateToGroup(group.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyGroupsView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.Class,
            contentDescription = null,
            modifier = Modifier.size(100.dp),
            tint = outlineVariantLight
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "No te has unido a ninguna clase todavía",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = onSurfaceLight,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Pide el código a tu profesor para empezar a aprender.",
            fontSize = 14.sp,
            color = onSurfaceVariantLight,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}
