package com.example.slnn3r.wallettrackerv2.ui.setting.settingview

import android.app.Activity
import android.app.TimePickerDialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.ui.setting.settingpresenter.SettingViewPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_setting.*
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.*

class SettingFragment : Fragment(), SettingViewInterface.SettingView {

    private val mSettingPresenter: SettingViewPresenter = SettingViewPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    private val mCurrentTime = Calendar.getInstance()
    private val hour = mCurrentTime.get(Calendar.HOUR_OF_DAY)
    private val minute = mCurrentTime.get(Calendar.MINUTE)
    private lateinit var simpleTimeFormat: SimpleDateFormat

    private var initialLaunch = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        simpleTimeFormat = SimpleDateFormat((Constant.Format.TIME_12HOURS_FORMAT), Locale.US)

        (activity as? AppCompatActivity)?.supportActionBar?.title =
                "Application Setting"
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackupSettingSwitchButton()
        setupReminderSettingSwitchButton()
        setupTimePicker()

        floatingActionButton.setOnClickListener {
            mSettingPresenter.saveSetting(context!!, userData.uid,
                    sb_setting_backupSetting.isChecked,
                    sb_setting_reminderSetting.isChecked,
                    et_setting_reminderTime.text.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        mSettingPresenter.bindView(this)

        if(initialLaunch){
            userData = mSettingPresenter.getSignedInUser()!!

            mSettingPresenter.checkBackupSetting(context!!, userData.uid)
            mSettingPresenter.checkReminderSetting(context!!, userData.uid)
        }
        initialLaunch=false
    }

    override fun onStop() {
        super.onStop()
        mSettingPresenter.unbindView()
    }

    override fun backupSettingToggle() {
        sb_setting_backupSetting.toggle()
    }

    override fun setBackupSettingEnable() {
        sb_setting_backupSetting.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
    }

    override fun setBackupSettingDisable() {
        sb_setting_backupSetting.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
    }

    override fun setReminderTime(reminderTime:String) {
        et_setting_reminderTime.setText(reminderTime)
    }


    override fun reminderSettingToggle() {
        sb_setting_reminderSetting.toggle()
    }

    override fun setReminderSettingEnable() {
        et_setting_reminderTime.isEnabled = true
        sb_setting_reminderSetting.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
    }

    override fun setReminderSettingDisable() {
        et_setting_reminderTime.isEnabled = false
        sb_setting_reminderSetting.backColor =
                ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
    }


    override fun saveSettingSuccess() {
        Snackbar.make(view!!,
                "App Setting Saved", Snackbar.LENGTH_SHORT)
                .show()
        (context as Activity).onBackPressed()
    }

    override fun onError(message: String) {
        Log.e(Constant.LoggingTag.SETTING_VIEW_LOGGING, message)
        mCustomErrorDialog.errorMessageDialog(context!!, message).show()
        return
    }

    private fun setupBackupSettingSwitchButton() {
        sb_setting_backupSetting.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            mSettingPresenter.checkBackupSwitchButton(sb_setting_backupSetting.isChecked)
        }
    }

    private fun setupReminderSettingSwitchButton() {
        sb_setting_reminderSetting.setOnCheckedChangeListener { _: CompoundButton, _: Boolean ->
            mSettingPresenter.checkReminderSwitchButton(sb_setting_reminderSetting.isChecked)
        }
    }

    private fun setupTimePicker() {
        val timeDialog = TimePickerDialog(context,
                TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
                    enableAllUiComponent()
                    (context as MenuActivity).setupNavigationMode()
                    val time = Time(selectedHour, selectedMinute, 0)
                    val formattedTime = simpleTimeFormat.format(time)
                    et_setting_reminderTime.setText(formattedTime)
                }, hour, minute, false)

        timeDialog.setOnCancelListener {
            enableAllUiComponent()
            (context as MenuActivity).setupNavigationMode()
        }

        et_setting_reminderTime.setOnClickListener {
            disableAllUiComponent()
            (context as MenuActivity).setupToDisable()
            timeDialog.show()
        }
    }

    private fun enableAllUiComponent(){
        sb_setting_reminderSetting.isEnabled = true
        sb_setting_backupSetting.isEnabled = true
        et_setting_reminderTime.isEnabled =true
    }

    private fun disableAllUiComponent(){
        sb_setting_reminderSetting.isEnabled = false
        sb_setting_backupSetting.isEnabled = false
        et_setting_reminderTime.isEnabled =false
    }
}
