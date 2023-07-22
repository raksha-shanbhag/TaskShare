package com.TaskShare.Models.Services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.TaskShare.Models.Repositories.TSUsersRepository
import com.example.greetingcard.MainActivity
import com.example.greetingcard.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.Random

class NotificationsService(): FirebaseMessagingService() {
    companion object {
        private val TAG = "Notifications"
        const val CHANNEL_ID = "TaskShare_channel"
        const val CHANNEL_NAME = "TaskShare_Notifications"
        const val CHANNEL_DESC = "Sends notifications for activities"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        if (TSUsersRepository.globalUserId.isEmpty()) {
            TSUsersRepository.setNotifTokenOnLogin = true
        } else {
            TSUsersRepository.setNotifToken(TSUsersRepository.globalUserId)
        }
    }

    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)

        val intent = Intent(this, MainActivity::class.java)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationID = Random().nextInt(3000)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setupChannels(notificationManager)
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        if (p0 != null) {
            Log.d(TAG, p0.toString())
            if (p0.data != null) {
                Log.d(TAG, p0.data.toString())
            }
        } else {
            Log.d(TAG, "PAIN")
        }

        val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notifications)
            .setContentTitle(p0?.data?.get("title"))
            .setContentText(p0?.data?.get("message"))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        notificationManager.notify(notificationID, notificationBuilder.build())
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun setupChannels(notificationManager: NotificationManager?) {
        val adminChannel: NotificationChannel =
            NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        adminChannel.description = CHANNEL_DESC
        adminChannel.enableLights(true)
        adminChannel.lightColor = Color.RED
        adminChannel.enableVibration(true)
        notificationManager?.createNotificationChannel(adminChannel)
    }
}
