package com.yashin.andrew.test.exchange.base

import android.widget.Toast
import com.arellomobile.mvp.MvpActivity
import com.yashin.andrew.test.exchange.application.ExchangeApp

abstract class BaseActivity : MvpActivity(), BaseView {

    fun getApp(): ExchangeApp = application as ExchangeApp

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}