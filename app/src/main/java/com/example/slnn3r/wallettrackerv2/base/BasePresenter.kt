package com.example.slnn3r.wallettrackerv2.base

import java.lang.ref.WeakReference

open class BasePresenter<V : BaseView> {

    lateinit var view: WeakReference<V>

    fun bindView(view: V) {
        this.view = WeakReference(view)
    }

    fun unbindView() {
        this.view.clear()
    }

    fun getView(): V? {
        return this.view.get()
    }
}