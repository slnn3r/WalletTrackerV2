package com.example.slnn3r.wallettrackerv2.ui.setting.settingpresenter

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.service.DataBackupJobService
import com.example.slnn3r.wallettrackerv2.ui.setting.settingview.SettingViewInterface
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


    override fun saveSetting(mContext: Context, userUid: String, backupSetting: Boolean) {

        baseModel.saveBackupSettingSharePreference(mContext, userUid, backupSetting)

        getView()!!.saveSettingSuccess()

        if (backupSetting) {
            startPeriodicallyBackup(mContext, userUid)
        } else {
            stopPeriodicallyBackup(mContext)
        }
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