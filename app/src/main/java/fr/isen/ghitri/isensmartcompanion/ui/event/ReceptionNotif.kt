package fr.isen.ghitri.isensmartcompanion.ui.event

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import fr.isen.ghitri.isensmartcompanion.R
import fr.isen.ghitri.isensmartcompanion.ui.event.Notificationfacilitator

class ReceptionNotif : BroadcastReceiver() {

    companion object {
        private const val TAG = "NotificationReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive called with intent: ${intent.extras}")

        val title = intent.getStringExtra("title") ?: "Event Reminder"
        val desc = intent.getStringExtra("desc") ?: "No details available"

        Notificationfacilitator.createNotificationChannelIfNeeded(context)

        val builder = NotificationCompat.Builder(context, Notificationfacilitator.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "Missing POST_NOTIFICATIONS permission; skipping notification.")
            return
        }


        NotificationManagerCompat.from(context).notify(title.hashCode(), builder.build())
        Log.d(TAG, "Notification posted for title: $title")
    }
}