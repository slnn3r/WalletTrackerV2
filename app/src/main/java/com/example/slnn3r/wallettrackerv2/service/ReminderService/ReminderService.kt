package com.example.slnn3r.wallettrackerv2.service.ReminderService

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import com.example.slnn3r.wallettrackerv2.R

class ReminderService : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        val mBuilder = NotificationCompat.Builder(context!!, "channel2")
                .setSmallIcon(R.drawable.ic_edit)
                .setContentTitle("Reminder to Do Record")
                .setContentText("Please record the Transactions that you have made.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Wallet Tracker Reminder"
            val descriptionText = "Notification About Record Transaction"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("channel2", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        with(NotificationManagerCompat.from(context)) {
            // notificationId is a unique int for each notification that you must define
            notify(2, mBuilder.build())
        }
    }

}