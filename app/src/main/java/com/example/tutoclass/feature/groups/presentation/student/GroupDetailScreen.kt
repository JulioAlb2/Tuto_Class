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

    var messageToEdit by remember { mutableStateOf<Message?>(null) }
    var messageToDelete by remember { mutableStateOf<Message?>(null) }
    var showEditDialog by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.messages.size) {
        if (state.messages.isNotEmpty()) {
            listState.animateScrollToItem(state.messages.size - 1)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                .consumeWindowInsets(padding)
                .background(TutoBgCanvas)
                .imePadding()
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
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
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
                                    .clip(RoundedCornerShape(16.dp))
                                    .combinedClickable(onClick = { showSheet = true }),
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
                                        Text("Alumnos Inscritos", fontWeight = FontWeight.Bold)
                                    }
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Surface(color = primaryContainerLight, shape = CircleShape) {
                                            Text(
                                                text = state.students.size.toString(),
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                                                fontSize = 12.sp, fontWeight = FontWeight.Bold, color = primaryLight
                                            )
                                        }
                                        Icon(Icons.Default.ChevronRight, null, tint = outlineVariantLight)
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        items(state.messages) { message ->
                            MessageBubble(
                                message = message,
                                isCurrentUser = message.userId.toString() == state.currentUser?.id,
                                onEditRequest = { 
                                    messageToEdit = message
                                    showEditDialog = true
                                },
                                onDeleteRequest = {
                                    messageToDelete = message
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }

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

        if (showEditDialog && messageToEdit != null) {
            var editedText by remember { mutableStateOf(messageToEdit!!.text) }
            AlertDialog(
                onDismissRequest = { showEditDialog = false },
                title = { Text("Editar Mensaje") },
                text = {
                    TextField(
                        value = editedText,
                        onValueChange = { editedText = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.updateMessage(messageToEdit!!.id, editedText)
                        showEditDialog = false
                    }) { Text("Guardar") }
                },
                dismissButton = {
                    TextButton(onClick = { showEditDialog = false }) { Text("Cancelar") }
                }
            )
        }

        if (showDeleteDialog && messageToDelete != null) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Eliminar Mensaje") },
                text = { Text("¿Estás seguro de que quieres eliminar este mensaje?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteMessage(messageToDelete!!.id)
                            showDeleteDialog = false
                        },
                        colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
                    ) { Text("Eliminar") }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") }
                }
            )
        }

        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false },
                sheetState = sheetState
            ) {
                Column(modifier = Modifier.fillMaxHeight(0.7f).padding(16.dp)) {
                    Text("Compañeros de Clase", fontWeight = FontWeight.Bold, fontSize = 20.sp)
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
    message: Message, 
    isCurrentUser: Boolean,
    onEditRequest: () -> Unit,
    onDeleteRequest: () -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

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
        
        Box {
            Surface(
                color = if (isCurrentUser) primaryLight else Color.White,
                shape = RoundedCornerShape(
                    topStart = 16.dp, topEnd = 16.dp,
                    bottomStart = if (isCurrentUser) 16.dp else 4.dp,
                    bottomEnd = if (isCurrentUser) 4.dp else 16.dp
                ),
                tonalElevation = 1.dp,
                shadowElevation = 1.dp,
                modifier = Modifier.combinedClickable(
                    onClick = { /* Opcional */ },
                    onLongClick = { if (isCurrentUser) showMenu = true }
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                    Text(
                        text = message.text,
                        color = if (isCurrentUser) Color.White else onSurfaceLight,
                        fontSize = 14.sp
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        if (message.edited) {
                            Text(
                                text = "editado ",
                                fontSize = 9.sp,
                                color = if (isCurrentUser) Color.White.copy(0.6f) else onSurfaceVariantLight
                            )
                        }
                        Text(
                            text = message.createdAt.substringAfter("T").take(5),
                            color = if (isCurrentUser) Color.White.copy(alpha = 0.7f) else onSurfaceVariantLight,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Editar") },
                    onClick = { 
                        showMenu = false
                        onEditRequest() 
                    },
                    leadingIcon = { Icon(Icons.Default.Edit, null) }
                )
                DropdownMenuItem(
                    text = { Text("Eliminar", color = Color.Red) },
                    onClick = { 
                        showMenu = false
                        onDeleteRequest() 
                    },
                    leadingIcon = { Icon(Icons.Default.Delete, null, tint = Color.Red) }
                )
            }
        }
    }
}

@Composable
fun ChatInput(text: String, onTextChange: (String) -> Unit, onSend: () -> Unit, isSending: Boolean) {
    Surface(
        color = Color.White, 
        tonalElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxWidth()
                .navigationBarsPadding(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text, onValueChange = onTextChange,
                placeholder = { Text("Escribe un mensaje...") },
                modifier = Modifier
                    .weight(1f)
                    .clip(CircleShape),
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent, 
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedContainerColor = surfaceContainerLight, 
                    unfocusedContainerColor = surfaceContainerLight
                ),
                maxLines = 4
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = onSend, enabled = text.isNotBlank() && !isSending,
                colors = IconButtonDefaults.iconButtonColors(containerColor = primaryLight, contentColor = Color.White)
            ) {
                if (isSending) CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
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
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = surfaceContainerLight) {
                Box(contentAlignment = Alignment.Center) {
                    Text(name.take(1).uppercase(), fontWeight = FontWeight.Bold, color = primaryLight)
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(name, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                Text(email, fontSize = 12.sp, color = onSurfaceVariantLight)
            }
        }
    }
}
