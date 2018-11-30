package com.example.slnn3r.wallettrackerv2.ui.menu.menupresenter

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.os.Handler
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.service.DataBackupJobService
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuViewInterface
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.auth.FirebaseAuth
import java.util.concurrent.TimeUnit

class MenuViewPresenter : MenuPresenterInterface.MenuViewPresenter,
        BasePresenter<MenuViewInterface.MenuView>() {

    private val baseModel: BaseModel = BaseModel()

    override fun executeGoogleSignOut(mGoogleSignInClient: GoogleSignInClient) {
        try {
            FirebaseAuth.getInstance().signOut()
        } catch (error: Exception) {
            getView()!!.onError(error.toString())
        }

        mGoogleSignInClient.revokeAccess()
                .addOnFailureListener {
                    getView()!!.onError(it.message.toString())
                    return@addOnFailureListener
                }

        mGoogleSignInClient.signOut()
                .addOnFailureListener {
                    getView()!!.onError(it.message.toString())
                    return@addOnFailureListener
                }

        getView()!!.signOutSuccess()
    }

    override fun navigationDrawerSelection(item: MenuItem) {

        getView()!!.closeDrawer()

        Handler().postDelayed({
            when (item.itemId) {
                R.id.nav_account -> {
                    getView()!!.proceedToAccountScreen()
                }

                R.id.nav_category -> {
                    getView()!!.proceedToCategoryScreen()
                }

                R.id.nav_history -> {
                    getView()!!.proceedToHistoryScreen()
                }

                R.id.nav_report -> {
                    getView()!!.proceedToReportScreen()
                }

                R.id.nav_backup -> {
                    getView()!!.executeBackupOnBackground()
                }

                R.id.nav_setting -> {
                    getView()!!.proceedToSettingScreen()
                }

                R.id.nav_sign_out -> {
                    getView()!!.proceedToSignOut()
                }
            }
        }, 300)
    }

    override fun checkNavigationStatus(isNavigated: String, isBackButton: Boolean, currentScreen: Int?,
                                       isOpenDrawer: Boolean, doubleBackToExitPressedOnce: Boolean) {
        // Check if Screen is navigated or not
        if (isNavigated == Constant.NavigationKey.NAV_MENU) {
            getView()!!.setupNavigationFlow()
        } else if (isNavigated == Constant.NavigationKey.NAV_DISABLE) {
            // Do Nothing for the ToolBar at Dialogfragment Display
        } else {
            if (isBackButton) {
                if (isOpenDrawer) {
                    getView()!!.closeDrawer()
                } else if (currentScreen == R.id.dashboardFragment) {
                    if (doubleBackToExitPressedOnce) {
                        getView()!!.superOnPressBack()
                    } else {
                        getView()!!.displayDoubleTabExitMessage()
                    }
                } else {
                    getView()!!.superOnPressBack() // not sure when will hit this?
                }
            } else {
                //displaySyncDateTime()
                getView()!!.openDrawer()
            }
        }
    }

    override fun checkBackupSetting(mContext: Context, userUid: String) {
        if(baseModel.getBackupSettingSharePreference(mContext, userUid)){
            getView()!!.executePeriodicalBackup()
        }
    }

    override fun checkBackupDateTime(mContext: Context, userUid: String) {
        val backupDateTime = baseModel.getBackupDateTimeSharePreference(mContext, userUid)
        if(backupDateTime!=""){
            getView()!!.updateDrawerBackupDateTime(backupDateTime)
        }
    }

    override fun backupDataManually(mContext: Context, userUid: String) {

        val bundle = PersistableBundle()
        bundle.putString(Constant.KeyId.JOBSERVICE_USERID_KEY, userUid)

        baseModel.saveBackTypeSharePreference(mContext, userUid, "Manual")

        val component = ComponentName(mContext, DataBackupJobService::class.java)
        val info = JobInfo.Builder(Constant.KeyId.JOBSERVICE_ID_KEY, component)
                .setExtras(bundle)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPersisted(true)
                .build()
        val scheduler: JobScheduler = mContext
                .applicationContext.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
        val resultCode = scheduler.schedule(info)

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            getView()!!.backupOnBackgroundStart()
        } else {
            getView()!!.onError("Job Schedule Failed")
        }
    }

    override fun backupDataPeriodically(mContext: Context, userUid: String) {

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

        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            getView()!!.backupPeriodicallyStart()
        } else {
            getView()!!.onError("Job Schedule Failed")
        }
    }

    override fun stopBackupDataPeriodically(mContext: Context) {
        val scheduler: JobScheduler = mContext
                .getSystemService(AppCompatActivity.JOB_SCHEDULER_SERVICE) as JobScheduler
        scheduler.cancel(Constant.KeyId.JOBSERVICE_ID_KEY)
    }
}