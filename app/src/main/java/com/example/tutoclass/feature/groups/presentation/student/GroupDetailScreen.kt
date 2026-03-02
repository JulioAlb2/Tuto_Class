package com.example.tutoclass.feature.groups.presentation.student

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.compose.*
import com.example.tutoclass.core.ui.theme.TutoBgCanvas
import com.example.tutoclass.feature.groups.domain.model.Message
import com.example.tutoclass.feature.groups.presentation.components.GroupDetailInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupDetailScreen(
    onBack: () -> Unit,
    viewModel: GroupDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }
    var messageText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    // Scroll al último mensaje cuando llega uno nuevo
    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text(
                            text = state.group?.name ?: "Cargando...",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        if (state.group != null) {
                            Text(
                                text = state.group!!.subject,
                                fontSize = 12.sp,
                                color = onSurfaceVariantLight
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(TutoBgCanvas)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                if (state.isLoading && state.group == null) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = primaryLight
                    )
                } else if (state.error != null && state.group == null) {
                    Text(
                        text = state.error ?: "Error desconocido",
                        color = errorLight,
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Info del Grupo y botón de alumnos
                        item {
                            state.group?.let { group ->
                                GroupDetailInfo(group = group)
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { showSheet = true },
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                elevation = CardDefaults.cardElevation(2.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.Group, null, tint = primaryLight)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            "Alumnos Inscritos", 
                                            fontWeight = FontWeight.Bold,
                                            color = onSurfaceLight
                                        )
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(
                                            color = primaryContainerLight,
                                            shape = CircleShape
                                        ) {
                                            Text(
                                                text = state.students.size.toString(),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = primaryLight
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(Icons.Default.ChevronRight, null, tint = outlineVariantLight)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                "Mensajes del Grupo",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = onSurfaceVariantLight,
                                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
                            )
                        }

                        // Lista de Mensajes
                        items(state.messages) { message ->
                            MessageBubble(
                                message = message,
                                isCurrentUser = message.userId.toString() == state.currentUser?.id
                            )
                        }
                    }
                }
            }

            // Input de Chat
            ChatInput(
                text = messageText,
                onTextChange = { messageText = it },
                onSend = {
                    viewModel.sendMessage(messageText)
                    messageText = ""
                },
                isSending = state.isSending
            )
        }

        // Modal para Lista de Alumnos
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState,
                containerColor = surfaceLight,
                dragHandle = { BottomSheetDefaults.DragHandle(color = outlineVariantLight) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxHeight(0.7f)
                        .padding(horizontal = 16.dp)
                ) {
                    Text(
                        text = "Compañeros de Clase",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = onSurfaceLight,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    if (state.students.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No hay más alumnos inscritos aún", color = onSurfaceVariantLight)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 32.dp)
                        ) {
                            items(state.students) { student ->
                                StudentItem(name = student.nombre, email = student.email)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: Message, isCurrentUser: Boolean) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        if (!isCurrentUser) {
            Text(
                text = message.userName,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = primaryLight,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )
        }
        
        Surface(
            color = if (isCurrentUser) primaryLight else Color.White,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                bottomEnd = if (isCurrentUser) 4.dp else 16.dp
            ),
            tonalElevation = 1.dp,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                Text(
                    text = message.text,
                    color = if (isCurrentUser) Color.White else onSurfaceLight,
                    fontSize = 14.sp
                )
                Text(
                    text = message.createdAt.substringAfter("T").take(5), // Formato simple HH:mm
                    color = if (isCurrentUser) Color.White.copy(alpha = 0.7f) else onSurfaceVariantLight,
                    fontSize = 10.sp,
                    modifier = Modifier.align(Alignment.End)
                )
            }
        }
    }
}

@Composable
fun ChatInput(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    isSending: Boolean
) {
    Surface(
        color = Color.White,
        tonalElevation = 8.dp,
        modifier = Modifier.imePadding()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("Escribe un mensaje...") },
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedContainerColor = surfaceContainerLight,
                    unfocusedContainerColor = surfaceContainerLight
                ),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank() && !isSending,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = primaryLight,
                    contentColor = Color.White,
                    disabledContainerColor = outlineVariantLight
                )
            ) {
                if (isSending) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
                }
            }
        }
    }
}

@Composable
fun StudentItem(name: String, email: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = surfaceContainerLight
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = name.take(1).uppercase(),
                        fontWeight = FontWeight.Bold,
                        color = primaryLight
                    )
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, fontWeight = FontWeight.Medium, fontSize = 14.sp, color = onSurfaceLight)
                Text(email, fontSize = 12.sp, color = onSurfaceVariantLight)
            }
        }
    }
}
