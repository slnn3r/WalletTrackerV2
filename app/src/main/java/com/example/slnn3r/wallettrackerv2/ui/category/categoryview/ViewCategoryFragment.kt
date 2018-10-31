package com.example.slnn3r.wallettrackerv2.ui.category.categoryview


import android.content.res.ColorStateList
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import kotlinx.android.synthetic.main.fragment_view_category.*

class ViewCategoryFragment : Fragment() {

    private var doubleBackToExitPressedOnce = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingActionButton4.setOnClickListener{

            if (doubleBackToExitPressedOnce) {
                val navController = view.findNavController()
                navController.navigate(R.id.action_viewCategoryFragment_to_detailsCategoryFragment)
                this.doubleBackToExitPressedOnce = false

            }else{
                val navController = view.findNavController()
                navController.navigate(R.id.action_viewCategoryFragment_to_createCategoryFragment)
                this.doubleBackToExitPressedOnce = true
            }


        }

        switch1.setOnCheckedChangeListener{ _: CompoundButton, _: Boolean ->

            if(switch1.isChecked){
                editTexttype.text = "Expense"
                switch1.backColor = ColorStateList.valueOf(resources.getColor(R.color.colorLightRed))

            }else{
                editTexttype.text = "Income"
                switch1.backColor = ColorStateList.valueOf(resources.getColor(R.color.colorLightGreen))
            }
        }

    }

}
