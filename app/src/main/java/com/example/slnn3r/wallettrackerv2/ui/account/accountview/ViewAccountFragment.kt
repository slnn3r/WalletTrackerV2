package com.example.slnn3r.wallettrackerv2.ui.account.accountview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import kotlinx.android.synthetic.main.fragment_view_account.*

class ViewAccountFragment : Fragment() {

    private var doubleBackToExitPressedOnce = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_view_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingActionButton2.setOnClickListener{

            if (doubleBackToExitPressedOnce) {
                val navController = view.findNavController()
                navController.navigate(R.id.action_viewAccountFragment_to_detailsAccountFragment)
                this.doubleBackToExitPressedOnce = false

            }else{
                val navController = view.findNavController()
                navController.navigate(R.id.action_viewAccountFragment_to_createAccountFragment)
                this.doubleBackToExitPressedOnce = true
            }
        }
    }
}
