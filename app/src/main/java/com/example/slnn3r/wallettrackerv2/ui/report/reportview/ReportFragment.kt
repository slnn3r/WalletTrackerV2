package com.example.slnn3r.wallettrackerv2.ui.report.reportview


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.slnn3r.wallettrackerv2.R

class ReportFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        (activity as? AppCompatActivity)?.supportActionBar?.title =
                getString(R.string.ab_report_title)
        return inflater.inflate(R.layout.fragment_report, container, false)
    }
}
