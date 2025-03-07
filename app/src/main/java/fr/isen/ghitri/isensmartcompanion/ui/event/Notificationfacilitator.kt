package fr.isen.ghitri.isensmartcompanion.ui.event

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import fr.isen.ghitri.isensmartcompanion.ui.event.EventModele
import fr.isen.ghitri.isensmartcompanion.ui.event.ReceptionNotif

object Notificationfacilitator {
    const val CHANNEL_ID = "event_channel"
    private const val TAG = "NotificationFacilitator"

    fun createNotificationChannelIfNeeded(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Event Reminders"
            val descriptionText = "Channel for event notifications"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            Log.d(TAG, "Creating notification channel: $CHANNEL_ID")
            notificationManager.createNotificationChannel(channel)
        } else {
            Log.d(TAG, "No need to create channel, SDK < O")
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    fun scheduleEventNotification(context: Context, event: EventModele, delayMillis: Long = 10_000L) {
        Log.d(TAG, "scheduleEventNotification called for event: ${event.title}, delay: $delayMillis ms")

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as android.app.AlarmManager

        val intent = Intent(context, ReceptionNotif::class.java).apply {
            putExtra("title", event.title)
            putExtra("desc", event.description)
        }

        val requestCode = event.id.hashCode()

        val pendingIntent = android.app.PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            android.app.PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = System.currentTimeMillis() + delayMillis
        Log.d(TAG, "Scheduling alarm at: $triggerTime (currentTime + $delayMillis)")

        alarmManager.setExactAndAllowWhileIdle(
            android.app.AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )
        Log.d(TAG, "Alarm setExactAndAllowWhileIdle called successfully.")
    }
}