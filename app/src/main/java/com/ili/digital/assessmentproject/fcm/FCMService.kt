package com.ili.digital.assessmentproject.fcm

import android.Manifest
import android.R
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ili.digital.assessmentproject.ui.MainActivity


class FCMService : FirebaseMessagingService() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(message: RemoteMessage) {

        val title: String = message.notification?.title!!
        val text: String = message.notification?.body!!


        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
                this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE
            )
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel(
                notificationManager
            ) else ""
        val notificationBuilder = NotificationCompat.Builder(this, channelId)



        val notification = notificationBuilder.setOngoing(true)
            .setContentTitle(title)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.drawable.ic_btn_speak_now)
            .setCategory(NotificationCompat.CATEGORY_SERVICE)
            .setAutoCancel(true)
            .build()


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        NotificationManagerCompat.from(this).notify(1, notification)

        super.onMessageReceived(message)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel(notificationManager: NotificationManager): String {
        val channelId = "my_service_channelid"
        val channelName = "My Foreground Service"
        val channel =
            NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
        // omitted the LED color
        channel.importance = NotificationManager.IMPORTANCE_HIGH
        channel.lockscreenVisibility = Notification.VISIBILITY_SECRET
        notificationManager.createNotificationChannel(channel)
        return channelId
    }

    override fun onMessageSent(msgId: String) {
        super.onMessageSent(msgId)
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}