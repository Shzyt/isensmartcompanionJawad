package fr.isen.ghitri.isensmartcompanion.ui

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.ghitri.isensmartcompanion.ui.event.EventCollect
import fr.isen.ghitri.isensmartcompanion.ui.event.EventDetailScreen
import fr.isen.ghitri.isensmartcompanion.ui.event.EventModele
import fr.isen.ghitri.isensmartcompanion.ui.event.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen() {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var events by remember { mutableStateOf<List<EventModele>>(emptyList()) }

    LaunchedEffect(Unit) {
        try {
            val service = RetrofitClient.createService(EventCollect::class.java)
            val result = withContext(Dispatchers.IO) { service.getEvents() }
            events = result
        } catch (e: Exception) {
            Toast.makeText(context, "Erreur : ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Événements à venir", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.mediumTopAppBarColors(containerColor = Color(0xFF6200EE), titleContentColor = Color.White)
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color(0xFF6200EE))
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(events) { event ->
                        EventItem(event) {
                            val intent = Intent(context, EventDetailScreen::class.java).apply {
                                putExtra("EVENT", event)
                            }
                            context.startActivity(intent)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventItem(event: EventModele, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = event.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6200EE)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "📅 ${event.date}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = "📍 ${event.location}",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
