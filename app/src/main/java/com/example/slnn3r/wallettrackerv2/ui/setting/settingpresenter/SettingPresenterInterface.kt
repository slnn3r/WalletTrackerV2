package com.example.slnn3r.wallettrackerv2.ui.setting.settingpresenter

import android.content.Context

interface SettingPresenterInterface {

    interface SettingViewPresenter{
        fun checkBackupSetting(mContext: Context, userUid: String)
        fun checkBackupSwitchButton(isChecked: Boolean)

        fun checkReminderSetting(mContext: Context, userUid: String)
        fun checkReminderSwitchButton(isChecked: Boolean)

        fun saveSetting(mContext: Context, userUid: String, backupSetting: Boolean,
                        reminderSetting: Boolean, reminderTime: String)
    }
}