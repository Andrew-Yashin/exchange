package com.yashin.andrew.test.exchange.base

import com.arellomobile.mvp.MvpPresenter
import io.reactivex.disposables.CompositeDisposable

abstract class BasePresenter<T : BaseView> : MvpPresenter<T>() {

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun detachView(view: T) {
        super.detachView(view)
        compositeDisposable.clear()
    }
}