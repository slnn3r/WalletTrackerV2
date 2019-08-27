package com.example.slnn3r.wallettrackerv2.ui.transaction.transactionadapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.slnn3r.wallettrackerv2.R

class RemarkAdapter(context: Context, resource: Int, remarkData: ArrayList<String>) : ArrayAdapter<String>(context, resource) {

    private var dataList: List<String>? = remarkData
    private val listFilter = ListFilter()
    private var dataListAllItems: List<String>? = null
    private var mListener: IOnItemListener? = null

    override fun getCount(): Int {
        return dataList!!.size
    }

    override fun getItem(position: Int): String? {
        return dataList!![position]
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var adapterView = view

        if (adapterView == null) {
            adapterView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.list_row_remark, parent, false)
        }

        val remarkTitleView = adapterView!!.findViewById(R.id.tv_remark_title) as TextView
        remarkTitleView.text = getItem(position)

        remarkTitleView.setOnLongClickListener {
            mListener?.closeKeyboard()
            mListener?.onLongClick(remarkTitleView.text.toString())
            true
        }

        remarkTitleView.setOnClickListener {
            mListener?.closeKeyboard()
            mListener?.onSingleClick(remarkTitleView.text.toString())
        }

        return adapterView
    }

    fun setListener(listener: IOnItemListener) {
        mListener = listener
    }

    interface IOnItemListener {
        fun onLongClick(remarkTitle: String)
        fun onSingleClick(remarkTitle: String)
        fun closeKeyboard()
    }

    override fun getFilter(): Filter {
        return listFilter
    }

    inner class ListFilter : Filter() {
        private val lock = Any()

        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            if (dataListAllItems == null) {
                synchronized(lock) {
                    dataListAllItems = ArrayList(dataList)
                }
            }

            if (prefix == null || prefix.isEmpty()) {
                synchronized(lock) {
                    results.values = dataListAllItems
                    results.count = dataListAllItems!!.size
                }
            } else {
                val searchStrLowerCase = prefix.toString()
                val matchValues = ArrayList<String>()

                for (dataItem in dataListAllItems!!) {
                    if (dataItem.contains(searchStrLowerCase, ignoreCase = true)) {
                        matchValues.add(dataItem)
                    }
                }

                results.values = matchValues
                results.count = matchValues.size
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.values != null) {
                dataList = results.values as ArrayList<String>
            } else {
                dataList = null
            }
            if (results.count > 0) {
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}
