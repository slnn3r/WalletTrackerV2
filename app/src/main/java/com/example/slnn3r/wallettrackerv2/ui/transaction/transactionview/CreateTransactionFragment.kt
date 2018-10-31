package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionview


import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.util.CustomCalculatorDialog
import kotlinx.android.synthetic.main.fragment_create_transaction.*


class CreateTransactionFragment : Fragment(),CustomCalculatorDialog.OnInputSelected {
    override fun calculatorInput(input: String) {

        (context as MenuActivity).setupNavigationMode()
        if (input == "") {
            etinlayouta.setText("Enter Amount")
        } else {
            etinlayouta.setText(input)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_transaction, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        etinlayouta.setOnClickListener {

            (context as MenuActivity).setupToDisable()

            val args = Bundle()
            if (etinlayouta.text.toString().toDoubleOrNull() != null) {
                args.putString("key", etinlayouta.text.toString())
            } else {
                args.putString("key", "")

            }

            etinlayouta.setText("Loading...")

            val calCustomDialog = CustomCalculatorDialog()
            calCustomDialog.arguments = args
            calCustomDialog.isCancelable = false
            calCustomDialog.setTargetFragment(this, 1)
            calCustomDialog.show(this.fragmentManager, "")
        }

        switchbobo.setOnCheckedChangeListener{ _: CompoundButton, _: Boolean ->

            if(switchbobo.isChecked){
                texttype.text = "Expense"
                switchbobo.backColor = ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))
                //texttype.setBackgroundResource(R.drawable.custom_lightred_background)
                //etinlayouta.setBackgroundResource(R.drawable.custom_lightred_background)

            }else{
                texttype.text = "Income"
                switchbobo.backColor = ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
                //texttype.setBackgroundResource(R.drawable.custom_lightgreen_background)
                //etinlayouta.setBackgroundResource(R.drawable.custom_lightgreen_background)
            }
        }
    }

}
