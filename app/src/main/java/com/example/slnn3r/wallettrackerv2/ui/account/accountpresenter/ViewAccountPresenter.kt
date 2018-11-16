package com.example.slnn3r.wallettrackerv2.ui.account.accountpresenter

import android.content.Context
import com.example.slnn3r.wallettrackerv2.base.BaseModel
import com.example.slnn3r.wallettrackerv2.base.BasePresenter
import com.example.slnn3r.wallettrackerv2.ui.account.accountview.AccountViewInterface

class ViewAccountPresenter : AccountPresenterInterface.ViewAccountPresenter,
        BasePresenter<AccountViewInterface.ViewAccountView>() {

    private val baseModel: BaseModel = BaseModel()

    override fun getAccountList(mContext: Context, userUid: String) {
        try {
            val dataList = baseModel.getAccListByUserUidSync(mContext, userUid)
            getView()!!.populateAccountRecycleView(dataList)
        } catch (e: Exception) {
            getView()!!.onError(e.message.toString())
        }
    }
}