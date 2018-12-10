package com.example.slnn3r.wallettrackerv2.ui.setting.settingview

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.example.slnn3r.wallettrackerv2.ui.setting.settingpresenter.SettingViewPresenter
import com.example.slnn3r.wallettrackerv2.util.CustomAlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : Fragment(), SettingViewInterface.SettingView {

    private val mSettingPresenter: SettingViewPresenter = SettingViewPresenter()
    private val mCustomErrorDialog: CustomAlertDialog = CustomAlertDialog()

    private lateinit var userData: FirebaseUser

    private var initialLaunch = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_setting_title)
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupBackupSettingSwitchButton()

        fb_setting_save.setOnClickListener {
            mSettingPresenter.saveSetting(context!!, userData.uid,
                    sb_setting_backupSetting.isChecked)
        }
    }

    override fun onStart() {
        super.onStart()
        mSettingPresenter.bindView(this)

        if (initialLaunch) {
            userData = mSettingPresenter.getSignedInUser()!!

            mSettingPresenter.checkBackupSetting(context!!, userData.uid)
        }
        initialLaunch = false
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

    override fun saveSettingSuccess() {
        Snackbar.make(view!!,
                getString(R.string.save_setting_message), Snackbar.LENGTH_SHORT)
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
}
