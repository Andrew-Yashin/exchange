package com.yashin.andrew.test.exchange.application.di

import android.content.Context
import com.yashin.andrew.test.exchange.main.di.MainComponent
import com.yashin.andrew.test.exchange.application.ExchangeApp
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun getApplicationContext() : Context

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(context: Context): Builder

        fun build(): ApplicationComponent
    }

    fun inject(exchangeApp : ExchangeApp)

    fun plusMainComponent() : MainComponent
}