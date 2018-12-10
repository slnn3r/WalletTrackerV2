package com.example.slnn3r.wallettrackerv2.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import android.util.Log
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.data.firebaseclass.AccountFirebase
import com.example.slnn3r.wallettrackerv2.data.firebaseclass.CategoryFirebase
import com.example.slnn3r.wallettrackerv2.data.firebaseclass.TransactionFirebase
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.example.slnn3r.wallettrackerv2.data.objectclass.Category
import com.example.slnn3r.wallettrackerv2.data.objectclass.Transaction
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.*

class DataBackupJobService : JobService() {

    private var jobCancelled: Boolean = false
    private val database = FirebaseDatabase.getInstance()
    private var dataBackupModel = DataBackupModel()
    private val baseModel: BaseModel = BaseModel()

    override fun onStartJob(params: JobParameters?): Boolean {
        doBackgroundWork(params!!)
        return true
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        jobCancelled = true
        return true
    }


    private fun updateTransactionCategory(categoryList: ArrayList<Category>) {
        categoryList.forEach { data ->
            database.reference.child(Constant.FirebasePushKey.CATEGORY_FIREBASE_KEY)
                    .push().setValue(data)
        }
    }

    private fun updateWalletAccount(accountList: ArrayList<Account>) {
        accountList.forEach { data ->
            database.reference.child(Constant.FirebasePushKey.ACCOUNT_FIREBASE_KEY)
                    .push().setValue(data)
        }
    }

    private fun updateTransaction(transactionList: ArrayList<Transaction>) {
        transactionList.forEach { data ->
            database.reference.child(Constant.FirebasePushKey.TRANSACTION_FIREBASE_KEY)
                    .push().setValue(data)
        }
    }

    //(No Error Handling for Firebase)
    private fun doBackgroundWork(params: JobParameters) {
        val userUid = params.extras.getString(Constant.KeyId.JOBSERVICE_USERID_KEY)

        val backupSetting = baseModel.getBackupSettingSharePreference(applicationContext, userUid!!)
        val backupType = baseModel.getBackupTypeSharePreference(applicationContext, userUid)

        val accountList = dataBackupModel.getAllAccountDataByUserUid(applicationContext, userUid)
        val categoryList = dataBackupModel.getAllCategoryDataByUserUid(applicationContext, userUid)
        val transactionList = dataBackupModel.getAllTransactionDataByUserUid(applicationContext, userUid)

        database.reference.child(Constant.FirebasePushKey.CATEGORY_FIREBASE_KEY)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {

                            val message = dataSnapshot.getValue(CategoryFirebase::class.java)

                            if (message!!.userUid == userUid) {
                                dataSnapshot.ref.setValue(null)
                            }
                        }

                        updateTransactionCategory(categoryList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("TAG: ", databaseError.message)
                    }
                })


        database.reference.child(Constant.FirebasePushKey.ACCOUNT_FIREBASE_KEY)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {

                            val message = dataSnapshot.getValue(AccountFirebase::class.java)

                            if (message!!.userUid == userUid) {
                                dataSnapshot.ref.setValue(null)
                            }
                        }

                        updateWalletAccount(accountList)

                        val df = SimpleDateFormat(Constant.Format.DATE_FORMAT, Locale.US)
                        val date12Format = SimpleDateFormat(Constant.Format.TIME_12HOURS_FORMAT, Locale.US)
                        val date = Calendar.getInstance().time
                        val formattedDate = df.format(date).toString()
                        val formattedTime = date12Format.format(date).toString()
                        baseModel.saveBackupDateTimeSharePreference(applicationContext, userUid,
                                applicationContext.getString(R.string.formatDisplayDateTime,
                                        formattedDate, formattedTime))

                        // pop out push notification
                        Handler().postDelayed({
                            createPushNotification(backupType, backupSetting, userUid)
                        }, 200)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("TAG: ", databaseError.message)
                    }
                })


        database.reference.child(Constant.FirebasePushKey.TRANSACTION_FIREBASE_KEY)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (dataSnapshot in snapshot.children) {

                            val message = dataSnapshot.getValue(TransactionFirebase::class.java)

                            if (message!!.account.userUid == userUid) {
                                dataSnapshot.ref.setValue(null)
                            }
                        }

                        updateTransaction(transactionList)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.w("TAG: ", databaseError.message)
                    }
                })
    }

    private fun createPushNotification(backupType: String, backupSetting: Boolean, userUid: String) {
        if (backupType == Constant.ConditionalKeyword.MANUAL_BACKUP) {
            val mBuilder = NotificationCompat.Builder(applicationContext, Constant.KeyId.NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_backup)
                    .setContentTitle(applicationContext.getString(R.string.backupDataCompleteTitle))
                    .setContentText(applicationContext.getString(R.string.backupDataCompleteMessage))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = applicationContext.getString(R.string.backupDataCompleteName)
                val descriptionText = applicationContext.getString(R.string.backupDataCompleteDesc)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(Constant.KeyId.NOTIFICATION_CHANNEL, name, importance)
                        .apply {
                            description = descriptionText
                        }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(Constant.KeyId.NOTIFICATION_ID, mBuilder.build())
            }

            baseModel.saveBackTypeSharePreference(applicationContext, userUid,
                    Constant.ConditionalKeyword.AUTO_BACKUP)

        } else if (backupType != Constant.ConditionalKeyword.MANUAL_BACKUP && backupSetting) {
            val stopBackupIntent = Intent(applicationContext, StopBackupJobService::class.java)

            val stopBackupPendingIntent: PendingIntent =
                    PendingIntent.getBroadcast(applicationContext, 0, stopBackupIntent, 0)

            val mBuilder = NotificationCompat.Builder(applicationContext, Constant.KeyId.NOTIFICATION_CHANNEL)
                    .setSmallIcon(R.drawable.ic_backup)
                    .setContentTitle(applicationContext.getString(R.string.backupDataCompleteTitle))
                    .setContentText(applicationContext.getString(R.string.backupDataCompleteMessage))
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(R.drawable.ic_stop, applicationContext.getString(R.string.backupDataCompleteAction),
                            stopBackupPendingIntent)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = applicationContext.getString(R.string.backupDataCompleteName)
                val descriptionText = applicationContext.getString(R.string.backupDataCompleteDesc)
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(Constant.KeyId.NOTIFICATION_CHANNEL,
                        name, importance).apply {
                    description = descriptionText
                }
                // Register the channel with the system
                val notificationManager: NotificationManager =
                        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            with(NotificationManagerCompat.from(applicationContext)) {
                // notificationId is a unique int for each notification that you must define
                notify(Constant.KeyId.NOTIFICATION_ID, mBuilder.build())
            }
        }
    }
}