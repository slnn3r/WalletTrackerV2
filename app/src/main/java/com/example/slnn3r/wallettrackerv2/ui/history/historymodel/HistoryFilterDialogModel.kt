package com.example.slnn3r.wallettrackerv2.ui.history.historymodel

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import com.example.slnn3r.wallettrackerv2.constant.Constant

class HistoryFilterDialogModel : HistoryModelInterface.HistoryFilterDialogModel {

    override fun getFilterInputSharePreference(mContext: Context): SharedPreferences {
        return mContext.getSharedPreferences(Constant.KeyId.FILTER_INPUT_SHARE_PREF,
                AppCompatActivity.MODE_PRIVATE)
    }

    override fun saveFilterInputSharePreference(mContext: Context, filterAccount: String,
                                                filterCatType: String,
                                                filterCategory: String,
                                                filterRemark: String,
                                                filterDateOption: String,
                                                filterDay: String,
                                                filterMonth: String,
                                                filterYear: String,
                                                filterStartDate: String,
                                                filterEndDate: String) {
        val editor = mContext.getSharedPreferences(Constant.KeyId.FILTER_INPUT_SHARE_PREF,
                AppCompatActivity.MODE_PRIVATE).edit()

        editor.putString(Constant.KeyId.FILTER_INPUT_ACCOUNT, filterAccount)
        editor.putString(Constant.KeyId.FILTER_INPUT_CATTYPE, filterCatType)
        editor.putString(Constant.KeyId.FILTER_INPUT_CATEGORY, filterCategory)
        editor.putString(Constant.KeyId.FILTER_INPUT_REMARK, filterRemark)
        editor.putString(Constant.KeyId.FILTER_INPUT_DATEOPTION, filterDateOption)
        editor.putString(Constant.KeyId.FILTER_INPUT_DAY, filterDay)
        editor.putString(Constant.KeyId.FILTER_INPUT_MONTH, filterMonth)
        editor.putString(Constant.KeyId.FILTER_INPUT_YEAR, filterYear)
        editor.putString(Constant.KeyId.FILTER_INPUT_STARTDATE, filterStartDate)
        editor.putString(Constant.KeyId.FILTER_INPUT_ENDDATE, filterEndDate)

        editor.apply()
        editor.commit()
    }
}