package com.yashin.andrew.test.exchange.main.view

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.snackbar.Snackbar
import com.jakewharton.rxbinding3.view.clicks
import com.yashin.andrew.test.exchange.R
import com.yashin.andrew.test.exchange.base.BaseActivity
import com.yashin.andrew.test.exchange.main.presenter.MainPresenter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity(), MainView {

    @InjectPresenter
    lateinit var presenter: MainPresenter

    private val fromButtonsArray = ArrayList<Button>()

    private val toButtonsArray = ArrayList<Button>()

    private var selectedItem: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fromButtonsArray.add(button_from_rub)
        fromButtonsArray.add(button_from_usd)
        fromButtonsArray.add(button_from_eur)
        fromButtonsArray.add(button_from_gbp)

        toButtonsArray.add(button_to_rub)
        toButtonsArray.add(button_to_usd)
        toButtonsArray.add(button_to_eur)
        toButtonsArray.add(button_to_gbp)

        button_from_rub.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.resolveCurrencyButtonClick(button_from_rub.text.toString(), false)
            }

        button_from_usd.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.resolveCurrencyButtonClick(button_from_usd.text.toString(), false)
            }

        button_from_eur.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.resolveCurrencyButtonClick(button_from_eur.text.toString(), false)
            }

        button_from_gbp.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.resolveCurrencyButtonClick(button_from_gbp.text.toString(), false)
            }

        button_to_rub.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.resolveCurrencyButtonClick(button_to_rub.text.toString(), true)
            }

        button_to_usd.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.resolveCurrencyButtonClick(button_to_usd.text.toString(), true)
            }

        button_to_eur.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.resolveCurrencyButtonClick(button_to_eur.text.toString(), true)
            }

        button_to_gbp.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                presenter.resolveCurrencyButtonClick(button_to_gbp.text.toString(), true)
            }

        button_from_other.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                showOtherCurrency(false)
            }

        button_to_other.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                showOtherCurrency(true)
            }

        make_change_button.clicks()
            .throttleFirst(500, TimeUnit.MILLISECONDS)
            .subscribe {
                try {
                    if (edit_text_input_money.text.isNullOrEmpty())
                        showToast("Введите количество меняемых денег!")
                    else presenter.getRate(edit_text_input_money.text.toString().toFloat())
                } catch (exception: NumberFormatException) {
                    showToast("Введено неверное значение меняемых денег!")
                }
            }
    }

    override fun changeButtonsColor(clickedButtonName: String, toOrFromButtonClicked: Boolean) {
        if (toOrFromButtonClicked) {
            for (button in toButtonsArray) {
                if (button.text.equals(clickedButtonName)) {
                    button.setBackgroundColor(getColor(R.color.green))
                    button.setTextColor(getColor(R.color.white))
                } else {
                    button.setBackgroundResource(R.drawable.currency_button_background)
                    button.setTextColor(getColor(R.color.black))
                }
            }
        } else {
            for (button in fromButtonsArray) {
                if (button.text.equals(clickedButtonName)) {
                    button.setBackgroundColor(getColor(R.color.green))
                    button.setTextColor(getColor(R.color.white))
                } else {
                    button.setBackgroundResource(R.drawable.currency_button_background)
                    button.setTextColor(getColor(R.color.black))
                }
            }
        }
    }

    override fun setOtherCurrencyButtonName(
        clickedButtonName: String,
        toOrFromButtonClicked: Boolean
    ) {
        if (toOrFromButtonClicked)
            button_to_gbp.text = clickedButtonName
        else
            button_from_gbp.text = clickedButtonName

    }

    override fun showChangedMoney(money: Float) {
        edit_text_output_money.text = money.toString()
    }

    override fun showOtherCurrency(toOrFromButtonClicked: Boolean) {
        AlertDialog.Builder(this)
            .setTitle("Выберите валюту")
            .setSingleChoiceItems(
                R.array.counties,
                selectedItem
            ) { _, i ->
                selectedItem = i
            }
            .setPositiveButton("OK") { dialog, _ ->
                presenter.resolveOtherCurrencyChoice(
                    resources.getStringArray(R.array.currencies)[selectedItem],
                    toOrFromButtonClicked
                )
                dialog.dismiss()
            }
            .setNegativeButton("ОТМЕНА") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()

    }

    override fun showEmptyDBSnackBar() {
        Snackbar.make(
                window.decorView.rootView,
                "Нет загруженных курсов",
                Snackbar.LENGTH_INDEFINITE
            )
            .setAction("Загрузить") {
                presenter.downloadRates()
            }
            .show()
    }

    override fun checkIsRatesDownloadComplete() {
        if (!getApp().downloadRatesStatus.hasValue()) {
            showProgress()
            getApp().downloadRatesStatus.hide()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    hideProgress()
                    presenter.checkCountOfRates()
                }, {
                    hideProgress()
                    presenter.checkCountOfRates()
                })
                .addTo(presenter.compositeDisposable)
        } else
            presenter.checkCountOfRates()
    }

    override fun showProgress() {
        layout_currency.visibility = View.GONE
        progress_bar_main.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress_bar_main.visibility = View.GONE
        layout_currency.visibility = View.VISIBLE
    }
}
