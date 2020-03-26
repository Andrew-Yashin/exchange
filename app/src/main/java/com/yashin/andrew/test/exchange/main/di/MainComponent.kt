package com.yashin.andrew.test.exchange.main.di

import com.yashin.andrew.test.exchange.main.presenter.MainPresenter
import com.yashin.andrew.test.exchange.main.view.MainActivity
import dagger.Subcomponent

@Subcomponent
interface MainComponent {
    fun inject(presenter : MainPresenter)
}