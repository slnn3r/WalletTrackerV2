package com.example.slnn3r.wallettrackerv2.util

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.slnn3r.wallettrackerv2.R
import kotlinx.android.synthetic.main.range_filter_custom_dialog.*

class CustomRangeFilterDialog : BottomSheetDialogFragment() {
    private lateinit var go: OnRangeFilter

    interface OnRangeFilter {
        fun rangeFilterInput(input: String)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.range_filter_custom_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog.findViewById<View>(android.support.design.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet!!)

            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                        behavior.state = BottomSheetBehavior.STATE_EXPANDED
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.peekHeight = 0
        }

        fb_filter_confirm.setOnClickListener {
            go.rangeFilterInput("gogo")
            dialog.dismiss()
        }

        fb_filter_cancel.setOnClickListener {
            go.rangeFilterInput("cancel")
            dialog.dismiss()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        try {
            go = this.targetFragment as OnRangeFilter
        } catch (e: ClassCastException) {
            Log.e("", e.toString())
        }
    }
}