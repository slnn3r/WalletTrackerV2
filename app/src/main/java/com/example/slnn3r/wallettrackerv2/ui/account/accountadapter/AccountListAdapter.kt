package com.example.slnn3r.wallettrackerv2.ui.account.accountadapter

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.string.Constant
import com.example.slnn3r.wallettrackerv2.data.objectclass.Account
import com.google.gson.Gson
import kotlinx.android.synthetic.main.account_list_row.view.*

var accAdapterClickCount = 0

class AccountListAdapter(private val accountList: ArrayList<Account>) :
        RecyclerView.Adapter<AccountViewHolder>() {

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        accAdapterClickCount = 0
    }

    override fun getItemCount(): Int {
        // +1 in order to create a empty list to resolve avoid floating button blocking issue
        return accountList.count() + 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val cellForRow = layoutInflater.inflate(R.layout.account_list_row, parent, false)
        return AccountViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        val viewContext = holder.view.context

        /* onBindHolder called several times as recycler news needs a view unless new one.
        So each time you set visilibity in child views, other views states are also changes.
        Whenever you scroll up and down, these views are getting re-drawed with wrong visibility options.
        SO USE setIsRecyclable(false) */
        holder.setIsRecyclable(false)

        if (position == accountList.count()) { // detect last index and use it create an empty list
            holder.view.cv_accListRow.background = null
            holder.view.cl_accListRow.isClickable = false
        } else {
            val accountData = accountList[position]

            if (accountData.accountStatus.equals(
                            Constant.ConditionalKeyword.DEFAULT_STATUS, ignoreCase = true)) {
                holder.view.tv_accListRow_accName.text = accountData.accountName
                holder.view.tv_accListRow_remark.text = viewContext.getString(R.string.non_deletable_title)
            } else {
                holder.view.tv_accListRow_accName.text = accountData.accountName
            }

            holder.passData = accountData
        }
    }
}

class AccountViewHolder(val view: View, var passData: Account? = null) :
        RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            if (passData != null && accAdapterClickCount < 1) {
                val gson = Gson()
                val accountData = Account(passData!!.accountId, passData!!.accountName,
                        passData!!.accountDesc, passData!!.userUid,
                        passData!!.accountStatus)

                val json = gson.toJson(accountData)

                val navController = view.findNavController()

                val bundle = Bundle()
                bundle.putString(Constant.KeyId.ACCOUNT_DETAILS_ARG, json)
                navController
                        .navigate(R.id.action_viewAccountFragment_to_detailsAccountFragment, bundle)

                accAdapterClickCount += 1
            }
        }
    }
}
