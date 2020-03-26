package com.yashin.andrew.test.exchange.main.presenter

import com.arellomobile.mvp.InjectViewState
import com.yashin.andrew.test.exchange.application.ExchangeApp
import com.yashin.andrew.test.exchange.base.BasePresenter
import com.yashin.andrew.test.exchange.data.entities.Rate
import com.yashin.andrew.test.exchange.main.model.MainModel
import com.yashin.andrew.test.exchange.main.view.MainView
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@InjectViewState
class MainPresenter : BasePresenter<MainView>() {

    @Inject
    lateinit var model: MainModel

    private var currentFromCurrency: String? = null

    private var currentToCurrency: String? = null

    init {
        ExchangeApp.component.plusMainComponent().inject(this)
    }

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.checkIsRatesDownloadComplete()
    }

    fun resolveCurrencyButtonClick(buttonCurrencyName: String, toOrFromButtonClicked: Boolean) {
        if (toOrFromButtonClicked)
            currentToCurrency = buttonCurrencyName
        else currentFromCurrency = buttonCurrencyName

        viewState.changeButtonsColor(buttonCurrencyName, toOrFromButtonClicked)
    }

    fun resolveOtherCurrencyChoice(buttonCurrencyName: String, toOrFromButtonClicked: Boolean) {
        if (toOrFromButtonClicked)
            currentToCurrency = buttonCurrencyName
        else currentFromCurrency = buttonCurrencyName

        viewState.setOtherCurrencyButtonName(buttonCurrencyName, toOrFromButtonClicked)
        viewState.changeButtonsColor(buttonCurrencyName, toOrFromButtonClicked)
    }

    fun checkCountOfRates(){
        model.getCount()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it == 0)
                    viewState.showEmptyDBSnackBar()
            }, {
                viewState.showToast("Произошла ошибка при взаимодействии с базой данных!\nПерезайдите в приложение..")
            })
            .addTo(compositeDisposable)
    }

    fun downloadRates() {
        viewState.showProgress()

        model.downloadAndSaveRates()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                viewState.hideProgress()
                if (it == 0)
                    viewState.showEmptyDBSnackBar()
            }, {
                viewState.hideProgress()
                viewState.showEmptyDBSnackBar()
                viewState.showToast("Произошла ошибка при обновлении данных\nПроверьте подключение к интернету..")
            })
            .addTo(compositeDisposable)
    }

    fun getRate(countOfCurrency: Float) {
        if (!currentFromCurrency.isNullOrEmpty() && !currentToCurrency.isNullOrEmpty()) {
            if (currentFromCurrency.equals(currentToCurrency)) {
                viewState.showChangedMoney(countOfCurrency)
            } else {
                val fromObs = model.getRate(currentFromCurrency!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                val toObs = model.getRate(currentToCurrency!!)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                Single.zip(
                        fromObs,
                        toObs,
                        BiFunction { firstRate: Rate, secondRate: Rate ->
                            return@BiFunction secondRate.course / firstRate.course
                        })
                    .subscribe({
                        viewState.showChangedMoney(countOfCurrency * it)
                    }, {

                    })
                    .addTo(compositeDisposable)
            }
        } else viewState.showToast("Выберите валюту!")
    }

}