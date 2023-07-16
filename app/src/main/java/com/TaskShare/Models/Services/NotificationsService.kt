package com.TaskShare.Models.Services

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.greetingcard.MainActivity
import com.example.greetingcard.R

class NotificationsService(
    private val context: Context
) {
    private val TAG = "Notifications"
    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    companion object {
        const val CHANNEL_ID = "TaskShare_channel"
        const val CHANNEL_NAME = "TaskShare_Notifications"
        const val CHANNEL_DESC = "Sends notifications for activities"
    }

    fun showNotification() {
        val myIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            1,
            myIntent,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )
        val broadcastIntent = PendingIntent.getActivity(
            context,
            2,
            Intent(context, NotificationReceiver::class.java),
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle("TEST TITLE")
            .setContentText("TEST NOTIFICATION")
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_notifications,
                "TEST",
                broadcastIntent
            )
            .build()

        notificationManager.notify(1, notification)
    }
}

class NotificationReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val service = NotificationsService(context)
        service.showNotification()
    }
}

class NotificationsApp: Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationsService.CHANNEL_ID,
                NotificationsService.CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = NotificationsService.CHANNEL_DESC

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
