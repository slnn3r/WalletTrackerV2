package com.example.slnn3r.wallettrackerv2.ui.setting.settingpresenter

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.service.BackupService.DataBackupJobService
import com.example.slnn3r.wallettrackerv2.service.ReminderService.ReminderService
import com.example.slnn3r.wallettrackerv2.ui.setting.settingview.SettingViewInterface
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class SettingViewPresenter : SettingPresenterInterface.SettingViewPresenter,
        BasePresenter<SettingViewInterface.SettingView>() {

    private val baseModel: BaseModel = BaseModel()

    override fun checkBackupSetting(mContext: Context, userUid: String) {
        if (!baseModel.getBackupSettingSharePreference(mContext, userUid)) {
            getView()!!.backupSettingToggle()
        }
    }

    override fun checkBackupSwitchButton(isChecked: Boolean) {
        if (isChecked) {
            getView()!!.setBackupSettingEnable()
        } else {
            getView()!!.setBackupSettingDisable()
        }
    }

    override fun checkReminderSetting(mContext: Context, userUid: String) {
        if (baseModel.getReminderSettingSharePreference(mContext, userUid)) {
            getView()!!.reminderSettingToggle()
            getView()!!.setReminderTime(baseModel.getReminderTimeSharePreference(mContext, userUid))
        }
    }

    override fun checkReminderSwitchButton(isChecked: Boolean) {
        if (isChecked) {
            getView()!!.setReminderSettingEnable()
        } else {
            getView()!!.setReminderSettingDisable()
        }
    }

    override fun saveSetting(mContext: Context, userUid: String, backupSetting: Boolean,
                             reminderSetting: Boolean, reminderTime: String) {

        baseModel.saveBackupSettingSharePreference(mContext, userUid, backupSetting)
        baseModel.saveReminderSettingSharePreference(mContext, userUid, reminderSetting)

        if(reminderSetting){
            baseModel.saveReminderTimeSharePreference(mContext, userUid, reminderTime)
        }

        getView()!!.saveSettingSuccess()

        if (backupSetting) {
            startPeriodicallyBackup(mContext, userUid)
        } else {
            stopPeriodicallyBackup(mContext)
        }

        if (reminderSetting) {
            startDailyReminder(mContext, reminderTime)
        } else {
            stopDailyReminder(mContext)
        }
    }

    private fun stopDailyReminder(mContext: Context) {
        val intent = Intent(mContext, ReminderService::class.java)
        val sender = PendingIntent.getBroadcast(mContext, 321, intent, 0)
        val alarmManager = mContext.getSystemService(ALARM_SERVICE) as AlarmManager?
        alarmManager!!.cancel(sender)
    }

    private fun startDailyReminder(mContext: Context, reminderTime: String) {
        val alarmMgr: AlarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(mContext, ReminderService::class.java)

        val notConvertedTime = reminderTime
        val date12Format = SimpleDateFormat(Constant.Format.TIME_12HOURS_FORMAT, Locale.US)
        val date24Format = SimpleDateFormat(Constant.Format.TIME_24HOURS_FORMAT, Locale.US)
        val convertedTime = date24Format.parse(date24Format.format(date12Format.parse(notConvertedTime)))

        val cal = Calendar.getInstance()
        cal.time = convertedTime
        val hours = cal.get(Calendar.HOUR_OF_DAY)
        val minutes = cal.get(Calendar.MINUTE)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, hours)
            set(Calendar.MINUTE, minutes)
            set(Calendar.SECOND,0)
        }

        val alarmIntent: PendingIntent = PendingIntent.getBroadcast(mContext, 321,intent,0)
        alarmMgr.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
        )
    }

    private fun startPeriodicallyBackup(mContext: Context, userUid: String) {
        val bundle = PersistableBundle()
        bundle.putString(Constant.KeyId.JOBSERVICE_USERID_KEY, userUid)

        baseModel.saveBackTypeSharePreference(mContext, userUid, "Auto")

        val component = ComponentName(mContext, DataBackupJobService::class.java)
        val info = JobInfo.Builder(Constant.KeyId.JOBSERVICE_ID_KEY, component)
                .setPeriodic(TimeUnit.MINUTES.toMillis(1))
                .setExtras(bundle)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()
        val scheduler: JobScheduler = mContext.applicationContext
                .getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)

        if (resultCode != JobScheduler.RESULT_SUCCESS) {
            getView()!!.onError("Job Schedule Failed")
        }
    }

    private fun stopPeriodicallyBackup(mContext: Context) {
        val scheduler: JobScheduler = mContext
                .getSystemService(AppCompatActivity.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(Constant.KeyId.JOBSERVICE_ID_KEY)
    }
}