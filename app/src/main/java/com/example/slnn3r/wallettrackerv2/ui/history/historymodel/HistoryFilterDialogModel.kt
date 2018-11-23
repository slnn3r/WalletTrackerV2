package com.example.slnn3r.wallettrackerv2.ui.history.historymodel

import android.content.Context
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity

class HistoryFilterDialogModel : HistoryModelInterface.HistoryFilterDialogModel {

    // SharePreference
    override fun getPreviousInputSharePreference(mContext: Context): SharedPreferences {
        return mContext.getSharedPreferences("PreviousInput",
                AppCompatActivity.MODE_PRIVATE)
    }

    override fun savePreviousInputSharePreference(mContext: Context, previousAccount: String,
                                                  previousCatType: String,
                                                  previousCategory: String,
                                                  previousRemark: String,
                                                  previousDateOption: String,
                                                  previousDay: String,
                                                  previousMonth: String,
                                                  previousYear: String,
                                                  previousStartDate: String,
                                                  previousEndDate: String) {
        val editor = mContext.getSharedPreferences("PreviousInput",
                AppCompatActivity.MODE_PRIVATE).edit()

        editor.putString("previousAccount", previousAccount)
        editor.putString("previousCatType", previousCatType)
        editor.putString("previousCategory", previousCategory)
        editor.putString("previousRemark", previousRemark)
        editor.putString("previousDateOption", previousDateOption)
        editor.putString("previousDay", previousDay)
        editor.putString("previousMonth", previousMonth)
        editor.putString("previousYear", previousYear)
        editor.putString("previousStartDate", previousStartDate)
        editor.putString("previousEndDate", previousEndDate)

        editor.apply()
        editor.commit()
    }

}