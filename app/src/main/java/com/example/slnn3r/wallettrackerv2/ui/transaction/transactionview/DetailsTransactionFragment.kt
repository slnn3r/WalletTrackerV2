package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.slnn3r.wallettrackerv2.R

class DetailsTransactionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_transaction, container, false)
    }


}
