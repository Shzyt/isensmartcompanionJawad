package fr.isen.ghitri.isensmartcompanion.ui.event

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.isen.ghitri.isensmartcompanion.ui.History.AppDataBase
import fr.isen.ghitri.isensmartcompanion.ui.theme.ThemeBonus
import kotlinx.coroutines.launch
import fr.isen.ghitri.isensmartcompanion.ui.event.Notificationfacilitator

class EventDetailScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val event = intent.getParcelableExtra<EventModele>("EVENT")
        setContent {
            ThemeBonus {
                Surface(color = MaterialTheme.colorScheme.background) {
                    EventDetailScreenContent(event, onBackPressed = { finish() })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreenContent(event: EventModele?, onBackPressed: () -> Unit) {
    val context = LocalContext.current
    if (event == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Événement indisponible", style = MaterialTheme.typography.bodyLarge)
        }
    } else {
        val db = AppDataBase.getDatabase(context)
        val notifEventDao = db.notifEventDao()
        val isPinnedCount by notifEventDao.isPinned(event.id).collectAsState(initial = 0)
        val isPinned = isPinnedCount > 0
        val scope = rememberCoroutineScope()

        fun togglePin() = scope.launch {
            if (!isPinned) {
                notifEventDao.insertPinnedEvent(event)
                Toast.makeText(context, "notification envoyee", Toast.LENGTH_SHORT).show()
            } else {
                notifEventDao.deleteByEventId(event.id)
                Toast.makeText(context, "Notification désactivée", Toast.LENGTH_SHORT).show()
            }
        }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(event.title) },
                    navigationIcon = {
                        IconButton(onClick = { onBackPressed() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(event.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                        Spacer(Modifier.height(16.dp))
                        Text("📅 Date : ${event.date}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(8.dp))
                        Text("📍 Lieu : ${event.location}", style = MaterialTheme.typography.bodyLarge)
                        Spacer(Modifier.height(8.dp))
                        Text("📌 Catégorie : ${event.category}", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { togglePin() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPinned) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Activer Notification",
                        tint = Color.White
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(if (isPinned) "Désactiver la notification" else "Activer la notification")
                }
            }
        }
    }
}
