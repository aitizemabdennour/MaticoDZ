
package com.matico.dz

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MaticoFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(
            "FCM",
            "MESSAGE REÇU ! from=${remoteMessage.from}"
        )

        Log.d(
            "FCM",
            "TITLE = ${remoteMessage.notification?.title}"
        )

        Log.d(
            "FCM",
            "BODY = ${remoteMessage.notification?.body}"
        )

        Log.d(
            "FCM",
            "DATA = ${remoteMessage.data}"
        )

        val title =
            remoteMessage.data["title"]
                ?: remoteMessage.notification?.title
                ?: "إشعار"

        val body =
            remoteMessage.data["body"]
                ?: remoteMessage.notification?.body
                ?: "لديك تحديث جديد"

        val reservationId =
            remoteMessage.data["reservation_id"]

        val intent = Intent(
            this,
            MainActivity::class.java
        ).apply {

            flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            putExtra(
                "reservation_id",
                reservationId
            )
        }

        val pendingIntent =
            PendingIntent.getActivity(
                this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            NotificationCompat.Builder(
                this,
                "reservations_v2"
            )
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build()

        val notificationManager =
            getSystemService(
                NotificationManager::class.java
            )
        Log.d(
            "FCM",
            "NOTIFICATIONS ACTIVES = ${
                notificationManager.activeNotifications
                    .filter { it.packageName == packageName }
                    .size
            }"
        )

        Log.d("FCM", "AVANT NOTIFY")
        Log.d(
            "FCM",
            "PACKAGE = $packageName"
        )

        Log.d(
            "FCM",
            "CHANNEL EXISTS = ${
                notificationManager.getNotificationChannel("reservations_v2") != null
            }"
        )


        notificationManager.notify(
            System.currentTimeMillis().toInt(),
            notification
        )
        Log.d(
            "FCM",
            "NOTIFICATION ACTIVE = ${notificationManager.areNotificationsEnabled()}"
        )

        Log.d(
            "FCM",
            "CHANNEL IMPORTANCE = ${
                notificationManager
                    .getNotificationChannel("reservations_v2")
                    ?.importance
            }"
        )

        Log.d("FCM", "APRÈS NOTIFY")
    }
}

