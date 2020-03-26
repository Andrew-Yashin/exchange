package com.yashin.andrew.test.exchange.base

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType

@StateStrategyType(value = SkipStrategy::class)
interface BaseView : MvpView {
    fun showToast(text: String)
    fun showProgress()
    fun hideProgress()
}