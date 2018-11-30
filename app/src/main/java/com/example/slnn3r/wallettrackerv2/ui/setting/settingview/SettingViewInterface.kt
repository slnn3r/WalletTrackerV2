package com.example.slnn3r.wallettrackerv2.ui.setting.settingview

import com.example.slnn3r.wallettrackerv2.base.BaseView

interface SettingViewInterface {

    interface SettingView: BaseView.Universal{
        fun backupSettingToggle()
        fun setBackupSettingEnable()
        fun setBackupSettingDisable()

        fun setReminderTime(reminderTime: String)

        fun reminderSettingToggle()
        fun setReminderSettingEnable()
        fun setReminderSettingDisable()

        fun saveSettingSuccess()
    }
}