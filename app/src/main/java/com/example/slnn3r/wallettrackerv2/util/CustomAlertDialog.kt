package com.example.slnn3r.wallettrackerv2.util

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.support.v7.app.AlertDialog
import com.example.slnn3r.wallettrackerv2.R

class CustomAlertDialog {

    fun confirmationDialog(mContext: Context, title: String, message: String, icon: Drawable,
                           dialogOnClickListener: DialogInterface.OnClickListener): Dialog {

        val confirmationDialog = AlertDialog.Builder(mContext)
        confirmationDialog.setTitle(title)
                .setIcon(icon)
                .setMessage(message)
                .setPositiveButton(mContext.getString(R.string.confirmation_dialog_positive_title)
                        , dialogOnClickListener)
                .setNegativeButton(mContext.getString(R.string.confirmation_dialog_negative_title)) { dialogBox, _ ->
                    dialogBox.dismiss()
                }
        return confirmationDialog.create()
    }

    fun errorMessageDialog(mContext: Context, message: String): Dialog {

        val errorMessageDialog = AlertDialog.Builder(mContext)
        errorMessageDialog.setTitle(mContext.getString(R.string.error_message_dialog_title))
                //.setIcon()
                .setCancelable(false)
                .setMessage(mContext.getString(R.string.error_message_dialog_message, message))
                .setPositiveButton(mContext.getString(R.string.error_message_dialog_positive_title)) { dialogBox, _ ->
                    dialogBox.dismiss()
                }
        return errorMessageDialog.create()
    }
}