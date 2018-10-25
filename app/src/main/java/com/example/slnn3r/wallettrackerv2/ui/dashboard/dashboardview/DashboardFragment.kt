package com.example.slnn3r.wallettrackerv2.ui.dashboard.dashboardview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment(), DashboardViewInterface.DashboardView {

    private var doubleBackToExitPressedOnce = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        floatingActionButton.setOnClickListener{

            (activity as MenuActivity).setupNavigationMode()

            if (doubleBackToExitPressedOnce) {
                val navController = view.findNavController()
                navController.navigate(R.id.action_dashboardFragment_to_detailsTransactionFragment)
                this.doubleBackToExitPressedOnce = false

            }else{
                val navController = view.findNavController()
                navController.navigate(R.id.action_dashboardFragment_to_createTransactionFragment)
                this.doubleBackToExitPressedOnce = true
            }
        }
    }

    override fun onError(message: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
