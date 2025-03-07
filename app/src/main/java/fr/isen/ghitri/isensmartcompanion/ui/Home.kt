package fr.isen.ghitri.isensmartcompanion.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.ai.client.generativeai.GenerativeModel
import fr.isen.ghitri.isensmartcompanion.ui.theme.ISENSmartCompanionTheme
import kotlinx.coroutines.launch
import fr.isen.ghitri.isensmartcompanion.ui.History.AppDatabase
import fr.isen.ghitri.isensmartcompanion.ui.History.ConversationHistoryEntity
import fr.isen.ghitri.isensmartcompanion.ui.History.ConversationHistoryDao

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    var userInput by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val messages = remember { mutableStateListOf<Pair<String, Boolean>>() }

    val generativeModel = remember {
        GenerativeModel(
            modelName = "gemini-1.5-flash",
            apiKey = "AIzaSyCMQRYDu4hELGQZqM6vD1IdBu_2JBLlcgw"
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "ISEN",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC30000),
            )
            Text(
                text = "Smart Companion",
                fontSize = 18.sp,
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f),
            reverseLayout = true // Scroll automatique vers le bas
        ) {
            items(messages.reversed()) { (message, isUser) ->
                MessageBubble(message, isUser)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Barre de saisie
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF2F2F7), RoundedCornerShape(20.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                placeholder = { Text("Pose ta question...") },
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (userInput.isNotBlank()) {
                        messages.add(userInput to true)

                        scope.launch {
                            try {
                                val response = StringBuilder()
                                generativeModel.generateContentStream(userInput).collect { chunk ->
                                    response.append(chunk.text ?: "")
                                }
                                messages.add(response.toString() to false)

                                val db = AppDatabase.getDatabase(context)
                                val dao = db.ConversationHistoryDao()
                                val message = ConversationHistoryEntity(
                                    question = userInput,
                                    answer = response.toString(),
                                    timestamp = System.currentTimeMillis()
                                )
                                dao.insertConversation(message)

                                userInput = ""
                            } catch (e: Exception) {
                                Toast.makeText(context, e.localizedMessage, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFC30000))
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Envoyer",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun MessageBubble(message: String, isUser: Boolean) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Text(
            text = message,
            fontSize = 16.sp,
            color = Color.White,
            modifier = Modifier
                .background(
                    if (isUser) Color(0xFF6200EE) else Color(0xFFE0E0E0),
                    shape = RoundedCornerShape(12.dp)
                )
                .padding(12.dp)
                .widthIn(max = 300.dp)
        )
    }
    Spacer(modifier = Modifier.height(8.dp))
}

