package com.example.slnn3r.wallettrackerv2.service

import android.app.job.JobScheduler
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import com.example.slnn3r.wallettrackerv2.constant.Constant

class StopBackupJobService : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val scheduler: JobScheduler = context
                .getSystemService(AppCompatActivity.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(Constant.KeyId.JOBSERVICE_ID_KEY)

        //This is used to close the notification tray
        val it = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
        context.sendBroadcast(it)

        // remove notification from notification tray
        NotificationManagerCompat.from(context).cancel(Constant.KeyId.NOTIFICATION_ID)

    }
}