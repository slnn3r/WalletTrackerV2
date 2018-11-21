package com.example.slnn3r.wallettrackerv2.ui.history.historyview

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.ui.menu.menuview.MenuActivity
import com.example.slnn3r.wallettrackerv2.util.CustomRangeFilterDialog
import com.example.slnn3r.wallettrackerv2.util.CustomSpecificFilterDialog
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter
import kotlinx.android.synthetic.main.fragment_history.*

class HistoryFragment : Fragment(), CustomSpecificFilterDialog.OnSpecificFilter, CustomRangeFilterDialog.OnRangeFilter {
    override fun rangeFilterInput(input: String) {
        (context as MenuActivity).setupNavigationMode()
        Toast.makeText(context, input,Toast.LENGTH_SHORT).show()
    }

    override fun specificFilterInput(input: String) {
        (context as MenuActivity).setupNavigationMode()
        Toast.makeText(context, input,Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_historyTrans_title)
        return inflater.inflate(R.layout.fragment_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val fragment = this

        fab_speed_dial43.setMenuListener(object : SimpleMenuListenerAdapter() {
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {

                when(menuItem.itemId){
                    R.id.fb_action_specific -> {

                        (context as MenuActivity).setupToDisable()
                        val calCustomDialog = CustomSpecificFilterDialog()
                        calCustomDialog.isCancelable = false
                        calCustomDialog.setTargetFragment(fragment, 1)
                        calCustomDialog.show(fragment.fragmentManager, "")
                    }
                    R.id.fb_action_range -> {
                        (context as MenuActivity).setupToDisable()
                        val calCustomDialog = CustomRangeFilterDialog()
                        calCustomDialog.isCancelable = false
                        calCustomDialog.setTargetFragment(fragment, 1)
                        calCustomDialog.show(fragment.fragmentManager, "")

                    }
                }

                return false
            }
        })

    }
}
