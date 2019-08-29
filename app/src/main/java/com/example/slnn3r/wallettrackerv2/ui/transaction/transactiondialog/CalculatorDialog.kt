package com.example.slnn3r.wallettrackerv2.ui.transaction.transactiondialog

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.example.slnn3r.wallettrackerv2.R
import com.example.slnn3r.wallettrackerv2.constant.Constant
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_calculator.*
import java.text.DecimalFormat

class CalculatorDialog : BottomSheetDialogFragment() {

    interface OnInputSelected {
        fun calculatorInput(input: String)
        fun calculatorNoInput()
    }

    private lateinit var inputSelection: OnInputSelected

    private var valueOne = java.lang.Double.NaN
    private var valueTwo: Double = 0.toDouble()

    private lateinit var addition: String
    private lateinit var subtraction: String
    private lateinit var multiplication: String
    private lateinit var division: String
    private var currentAction = ""

    private var rewriting = false

    private var isOperatorClicked = false

    private lateinit var decFormat: DecimalFormat

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addition = getString(R.string.btnPlus_label)
        subtraction = getString(R.string.btnMinus_label)
        multiplication = getString(R.string.btnMultiply_label)
        division = getString(R.string.btnDivide_label)
        decFormat = DecimalFormat(Constant.Format.DECIMAL_FORMAT)

        return inflater.inflate(R.layout.dialog_calculator, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.viewTreeObserver.addOnGlobalLayoutListener {
            val dialog = dialog as BottomSheetDialog
            val bottomSheet = dialog
                    .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) as FrameLayout?
            val behavior = BottomSheetBehavior.from(bottomSheet)

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

        val mArgs = this.arguments
        val myValue = mArgs?.getString(Constant.KeyId.CALCULATE_DIALOG_ARG)

        if (myValue?.toDoubleOrNull() != null) {
            tv_calCustomDialog_amount.setText(myValue)
        } else {
            tv_calCustomDialog_amount.setText("")
        }

        tv_calCustomDialog_amount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("", "")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val text = tv_calCustomDialog_amount.text.toString()
                if (text.contains(".") &&
                        text.substring(text.indexOf(".") + 1).length > 2) {
                    tv_calCustomDialog_amount.setText(text.substring(0, text.length - 1))
                    tv_calCustomDialog_amount.setSelection(tv_calCustomDialog_amount.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {
                Log.d("", "")
            }
        })

        btn_confirm_calCustomDialog.setOnClickListener {
            if (tv_calCustomDialog_amount.text.toString() == getString(R.string.notANumber_label)
                    || tv_calCustomDialog_amount.text.toString() == "") {
                inputSelection.calculatorNoInput()
            } else {
                if (tv_calCustomDialog_amount.text.toString().toDouble() > 0) {
                    inputSelection.calculatorInput(tv_calCustomDialog_amount.text.toString())
                } else {
                    inputSelection.calculatorNoInput()
                }
            }

            dialog?.dismiss()
        }

        btn_cancel_calCustomDialog.setOnClickListener {
            if (myValue?.toDoubleOrNull() != null) {
                inputSelection.calculatorInput(myValue)
                dialog?.dismiss()
            } else {
                inputSelection.calculatorNoInput()
                dialog?.dismiss()
            }
        }

        btn_0_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.ZERO)

            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.ZERO)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_1_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.ONE)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.ONE)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_2_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.TWO)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.TWO)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_3_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.THREE)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.THREE)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_4_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.FOUR)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.FOUR)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_5_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.FIVE)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.FIVE)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_6_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.SIX)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.SIX)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_7_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.SEVEN)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.SEVEN)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_8_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.EIGHT)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.EIGHT)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_9_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(Constant.Calculator.NINE)
            } else {
                tv_calCustomDialog_amount.setText(Constant.Calculator.NINE)
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_dot_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setTextColor(Color.BLACK)

            if (!rewriting) {
                tv_calCustomDialog_amount.text =
                        tv_calCustomDialog_amount.text.append(btn_dot_calCustomDialog.text)
            } else {
                tv_calCustomDialog_amount.setText(btn_dot_calCustomDialog.text.toString())
                rewriting = false
            }
            isOperatorClicked = false
        }

        btn_delete_calCustomDialog.setOnClickListener {
            if (!rewriting) {
                val length = tv_calCustomDialog_amount.text.length
                if (length > 0) {
                    tv_calCustomDialog_amount.text.delete(length - 1, length)
                }
            }
        }

        btn_clear_calCustomDialog.setOnClickListener {
            tv_calCustomDialog_amount.setText("")
            resetCalculator()
        }

        btn_plus_calCustomDialog.setOnClickListener {
            btn_plus_calCustomDialog.setTextColor(resources.getColor(R.color.colorPrimary))
            btn_minus_calCustomDialog.setTextColor(Color.BLACK)
            btn_multiply_calCustomDialog.setTextColor(Color.BLACK)
            btn_divide_calCustomDialog.setTextColor(Color.BLACK)

            if (isOperatorClicked){
                currentAction = addition
                return@setOnClickListener
            }

            computeCalculation()
            currentAction = addition

            tv_calCustomDialog_amount.setText(decFormat.format(valueOne))
            tv_calCustomDialog_amount.setTextColor(resources.getColor(R.color.colorPrimary))
            rewriting = true
            isOperatorClicked = true
        }

        btn_minus_calCustomDialog.setOnClickListener {
            btn_plus_calCustomDialog.setTextColor(Color.BLACK)
            btn_minus_calCustomDialog.setTextColor(resources.getColor(R.color.colorPrimary))
            btn_multiply_calCustomDialog.setTextColor(Color.BLACK)
            btn_divide_calCustomDialog.setTextColor(Color.BLACK)

            if (isOperatorClicked){
                currentAction = subtraction
                return@setOnClickListener
            }

            computeCalculation()
            currentAction = subtraction

            tv_calCustomDialog_amount.setText(decFormat.format(valueOne))
            tv_calCustomDialog_amount.setTextColor(resources.getColor(R.color.colorPrimary))
            rewriting = true
            isOperatorClicked = true
        }

        btn_multiply_calCustomDialog.setOnClickListener {
            btn_plus_calCustomDialog.setTextColor(Color.BLACK)
            btn_minus_calCustomDialog.setTextColor(Color.BLACK)
            btn_multiply_calCustomDialog.setTextColor(resources.getColor(R.color.colorPrimary))
            btn_divide_calCustomDialog.setTextColor(Color.BLACK)

            if (isOperatorClicked){
                currentAction = multiplication
                return@setOnClickListener
            }

            computeCalculation()
            currentAction = multiplication

            tv_calCustomDialog_amount.setText(decFormat.format(valueOne))
            tv_calCustomDialog_amount.setTextColor(resources.getColor(R.color.colorPrimary))
            rewriting = true
            isOperatorClicked = true
        }

        btn_divide_calCustomDialog.setOnClickListener {
            btn_plus_calCustomDialog.setTextColor(Color.BLACK)
            btn_minus_calCustomDialog.setTextColor(Color.BLACK)
            btn_multiply_calCustomDialog.setTextColor(Color.BLACK)
            btn_divide_calCustomDialog.setTextColor(resources.getColor(R.color.colorPrimary))

            if (isOperatorClicked){
                currentAction = division
                return@setOnClickListener
            }

            computeCalculation()
            currentAction = division

            tv_calCustomDialog_amount.setText(decFormat.format(valueOne))
            tv_calCustomDialog_amount.setTextColor(resources.getColor(R.color.colorPrimary))
            rewriting = true
            isOperatorClicked = true
        }

        btn_equal_calCustomDialog.setOnClickListener {
            computeCalculation()
            tv_calCustomDialog_amount.setText(decFormat.format(valueOne))
            tv_calCustomDialog_amount.setTextColor(resources.getColor(R.color.colorPrimary))
            resetCalculator()
        }
    }

    private fun computeCalculation() {
        if (!java.lang.Double.isNaN(valueOne)) {
            valueTwo = if (tv_calCustomDialog_amount.text.toString().toDoubleOrNull() == null) {
                Double.NaN
            } else {
                tv_calCustomDialog_amount.text.toString().toDouble()
            }

            tv_calCustomDialog_amount.text = null

            when (currentAction) {
                addition -> {
                    valueOne += valueTwo
                    checkExcessiveAmount()
                }
                subtraction -> {
                    valueOne -= valueTwo
                    checkExcessiveAmount()
                }
                multiplication -> {
                    valueOne *= valueTwo
                    checkExcessiveAmount()
                }
                division -> {
                    valueOne /= valueTwo
                    checkExcessiveAmount()
                }
            }
        } else {
            try {
                valueOne = tv_calCustomDialog_amount.text.toString().toDouble()
            } catch (e: Exception) {
            }
        }
    }

    private fun checkExcessiveAmount() {
        if (valueOne > Constant.Calculator.MAX_AMOUNT || valueOne < Constant.Calculator.MIN_AMOUNT) {
            valueOne = Double.NaN
            tv_calCustomDialog_amount.setText(decFormat.format(valueOne))
            tv_calCustomDialog_amount.setTextColor(resources.getColor(R.color.colorPrimary))
            currentAction = ""
            rewriting = false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            inputSelection = this.targetFragment as OnInputSelected
        } catch (e: ClassCastException) {
            Log.e("", e.toString())
        }
    }

    private fun resetCalculator() {
        btn_plus_calCustomDialog.setTextColor(Color.BLACK)
        btn_minus_calCustomDialog.setTextColor(Color.BLACK)
        btn_multiply_calCustomDialog.setTextColor(Color.BLACK)
        btn_divide_calCustomDialog.setTextColor(Color.BLACK)
        valueOne = Double.NaN
        currentAction = ""
        rewriting = true
        isOperatorClicked = false
    }
}