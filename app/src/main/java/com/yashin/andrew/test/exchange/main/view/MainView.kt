package com.yashin.andrew.test.exchange.main.view

import com.arellomobile.mvp.viewstate.strategy.SkipStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.yashin.andrew.test.exchange.base.BaseView

interface MainView : BaseView {
    fun showChangedMoney(money: Float)
    fun setOtherCurrencyButtonName(clickedButtonName: String, toOrFromButtonClicked: Boolean)
    fun changeButtonsColor(clickedButtonName: String, toOrFromButtonClicked: Boolean)
    fun showOtherCurrency(toOrFromButtonClicked: Boolean)

    @StateStrategyType(value = SkipStrategy::class)
    fun showEmptyDBSnackBar()
    @StateStrategyType(value = SkipStrategy::class)
    fun checkIsRatesDownloadComplete()
}