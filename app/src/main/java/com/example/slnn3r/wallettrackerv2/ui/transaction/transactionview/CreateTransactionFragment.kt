package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview


import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.TextView

import com.example.slnn3r.wallettrackerv2.R
import kotlinx.android.synthetic.main.fragment_create_transaction.*


class CreateTransactionFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        switchbobo.setOnCheckedChangeListener{ _: CompoundButton, _: Boolean ->

            if(switchbobo.isChecked){
                texttype.setText("Expense", TextView.BufferType.EDITABLE)
                switchbobo.backColor = ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
                texttype.setBackgroundResource(R.drawable.custom_lightred_background)
                editTextdiam.setBackgroundResource(R.drawable.custom_lightred_background)

            }else{
                texttype.setText("Income", TextView.BufferType.EDITABLE)
                switchbobo.backColor = ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
                texttype.setBackgroundResource(R.drawable.custom_lightgreen_background)
                editTextdiam.setBackgroundResource(R.drawable.custom_lightgreen_background)
            }
        }
    }

}
