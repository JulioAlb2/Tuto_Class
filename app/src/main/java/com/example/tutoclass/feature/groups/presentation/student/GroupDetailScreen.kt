package com.example.tutoclass.feature.groups.presentation.student

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Delete
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

    var messageToEdit by remember { mutableStateOf<Message?>(null) }
    var messageToDelete by remember { mutableStateOf<Message?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(0)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Chat del Grupo",
                            fontWeight = FontWeight.Bold, fontSize = 18.sp,
                            color = onSurfaceLight
                        )
                        Text(
                            text = "Comunidad TutoClass",
                            fontSize = 12.sp, 
                            color = onSurfaceVariantLight
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = primaryLight)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = surfaceLight)
            )
        },
        bottomBar = {
            ChatInput(
                text = messageText,
                onTextChange = { messageText = it },
                onSend = {
                    if (messageText.isNotBlank()) {
                        viewModel.sendMessage(messageText)
                        messageText = ""
                    }
                },
                isSending = state.isSending
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(surfaceLight)
        ) {
            // 1. Cabecera con Información del Grupo
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                state.group?.let { group ->
                    GroupDetailInfo(group = group)
                    Spacer(modifier = Modifier.height(12.dp))
                }
                
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .combinedClickable(onClick = { showSheet = true }),
                    colors = CardDefaults.cardColors(containerColor = surfaceContainerLight),
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Group, null, tint = primaryLight, modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Text("Alumnos Inscritos", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = onSurfaceLight)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(color = primaryContainerLight, shape = CircleShape) {
                                Text(
                                    text = state.students.size.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryLight
                                )
                            }
                            Icon(Icons.Default.ChevronRight, null, tint = outlineVariantLight)
                        }
                    }
                }
            }

            HorizontalDivider(modifier = Modifier.padding(top = 8.dp), color = outlineVariantLight.copy(0.3f))

            // 2. Contenedor del Chat
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(onBackgroundDark)
            ) {
                if (state.isLoading && state.group == null) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center), color = primaryLight)
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        reverseLayout = true,
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(state.messages.asReversed(), key = { it.id }) { message ->
                            MessageBubble(
                                message = message,
                                isCurrentUser = message.userId.toString() == state.currentUser?.id,
                                onEditRequest = { messageToEdit = message; showEditDialog = true },
                                onDeleteRequest = { messageToDelete = message; showDeleteDialog = true }
                            )
                        }
                        item { Spacer(modifier = Modifier.height(16.dp)) }
                    }
                }
            }
        }

        // Diálogos de Edición y Borrado
        if (showEditDialog && messageToEdit != null) {
            var editedText by remember { mutableStateOf(messageToEdit!!.text) }
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Editar Mensaje", color = onSurfaceLight) },
                text = { 
                    TextField(
                        value = editedText, 
                        onValueChange = { editedText = it }, 
                        modifier = Modifier.fillMaxWidth(),
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = onSurfaceLight,
                            unfocusedTextColor = onSurfaceLight,
                            focusedContainerColor = surfaceContainerLight,
                            unfocusedContainerColor = surfaceContainerLight
                        )
                    ) 
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.updateMessage(messageToEdit!!.id, editedText); showEditDialog = false }) { 
                        Text("Guardar", color = primaryLight) 
                    }
                },
                dismissButton = { 
                    TextButton(onClick = { showEditDialog = false }) { 
                        Text("Cancelar", color = onSurfaceVariantLight) 
                    } 
                },
                containerColor = surfaceLight
            )
        }

        if (showDeleteDialog && messageToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar Mensaje", color = onSurfaceLight) },
                text = { Text("¿Estás seguro de que quieres eliminar este mensaje?", color = onSurfaceVariantLight) },
                confirmButton = {
                    TextButton(
                        onClick = { viewModel.deleteMessage(messageToDelete!!.id); showDeleteDialog = false },
                        colors = ButtonDefaults.textButtonColors(contentColor = errorLight)
                    ) { Text("Eliminar") }
                },
                dismissButton = { 
                    TextButton(onClick = { showDeleteDialog = false }) { 
                        Text("Cancelar", color = onSurfaceVariantLight) 
                    } 
                },
                containerColor = surfaceLight
            )
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false }, 
                sheetState = sheetState,
                containerColor = surfaceLight
            ) {
                Column(modifier = Modifier.fillMaxHeight(0.7f).padding(16.dp)) {
                    Text("Compañeros de Clase", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = onSurfaceLight)
                    Spacer(modifier = Modifier.height(16.dp))
                    LazyColumn {
                        items(state.students) { student -> StudentItem(student.nombre, student.email) }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MessageBubble(
    message: Message, isCurrentUser: Boolean,
    onEditRequest: () -> Unit, onDeleteRequest: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isCurrentUser) Alignment.End else Alignment.Start
    ) {
        if (!isCurrentUser) {
            Text(
                text = message.userName, fontSize = 11.sp, fontWeight = FontWeight.Bold,
                color = primaryLight,
                modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
            )
        }
        Box {
            Surface(
                color = if (isCurrentUser) primaryLight else surfaceContainerLowestLight,
                shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp,
                    bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                    bottomEnd = if (isCurrentUser) 4.dp else 16.dp
                ),
                tonalElevation = 1.dp, shadowElevation = 1.dp,
                modifier = Modifier.combinedClickable(
                    onClick = { },
                    onLongClick = { if (isCurrentUser) showMenu = true }
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(
                        text = message.text, 
                        color = if (isCurrentUser) onPrimaryLight else onSurfaceLight, 
                        fontSize = 14.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        if (message.edited) {
                            Text(
                                text = "editado ", 
                                fontSize = 9.sp, 
                                color = if (isCurrentUser) onPrimaryLight.copy(0.6f) else onSurfaceVariantLight
                            )
                        }
                        Text(
                            text = message.createdAt.substringAfter("T").take(5),
                            color = if (isCurrentUser) onPrimaryLight.copy(alpha = 0.7f) else onSurfaceVariantLight,
                            fontSize = 10.sp
                        )
                    }
                }
            }
            DropdownMenu(
                expanded = showMenu, 
                onDismissRequest = { showMenu = false },
                modifier = Modifier.background(surfaceLight)
            ) {
                DropdownMenuItem(
                    text = { Text("Editar", color = onSurfaceLight) }, 
                    onClick = { showMenu = false; onEditRequest() }, 
                    leadingIcon = { Icon(Icons.Default.Edit, null, tint = primaryLight) }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar", color = errorLight) }, 
                    onClick = { showMenu = false; onDeleteRequest() }, 
                    leadingIcon = { Icon(Icons.Default.Delete, null, tint = errorLight) }
                )
            }
        }
    }
}

@Composable
fun ChatInput(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit, isSending: Boolean) {
    Surface(
        color = surfaceLight,
        tonalElevation = 8.dp,
        modifier = Modifier.fillMaxWidth().imePadding()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 8.dp)
                .fillMaxWidth()
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text("Escribe un mensaje...", color = onSurfaceVariantLight.copy(alpha = 0.7f))
                },
                modifier = Modifier
                    .weight(1f)
                    .heightIn(min = 52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = onSurfaceLight,
                    unfocusedTextColor = onSurfaceLight,
                    focusedContainerColor = surfaceContainerLight,
                    unfocusedContainerColor = surfaceContainerLight,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = primaryLight
                ),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSend,
                enabled = text.isNotBlank() && !isSending,
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = primaryLight, 
                    contentColor = onPrimaryLight,
                    disabledContainerColor = outlineVariantLight
                ),
                modifier = Modifier.size(48.dp)
            ) {
                if (isSending) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = onPrimaryLight, strokeWidth = 2.dp)
                else Icon(Icons.AutoMirrored.Filled.Send, null)
            }
        }
    }
}

@Composable
fun StudentItem(name: String, email: String) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        shape = RoundedCornerShape(12.dp), 
        colors = CardDefaults.cardColors(containerColor = surfaceContainerLowestLight),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = primaryContainerLight) {
                Box(contentAlignment = Alignment.Center) {
                    Text(name.take(1).uppercase(), fontWeight = FontWeight.Bold, color = primaryLight)
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
