package com.yashin.andrew.test.exchange.application

import android.app.Application
import com.yashin.andrew.test.exchange.application.di.ApplicationComponent
import com.yashin.andrew.test.exchange.application.di.DaggerApplicationComponent
import com.yashin.andrew.test.exchange.data.RatesDatabase
import com.yashin.andrew.test.exchange.data.entities.Rate
import com.yashin.andrew.test.exchange.network.DownloadRatesInteractor
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class ExchangeApp : Application() {

    @Inject
    lateinit var downloadRatesInteractor: DownloadRatesInteractor

    @Inject
    lateinit var ratesDatabase: RatesDatabase

    var downloadRatesStatus = BehaviorSubject.create<Boolean>()

    companion object {
        lateinit var component: ApplicationComponent
    }

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent
            .builder()
            .application(this)
            .build()
        component.inject(this)
        downloadAllRates()
    }

    private fun downloadAllRates() {
        downloadRatesInteractor.downloadAllRates(null)
            .subscribe({
                ratesDatabase.runInTransaction {
                    ratesDatabase.rateDao().deleteAll()
                    it.rates.forEach {
                        ratesDatabase.rateDao().insert(Rate(it.key, it.value))
                    }
                    downloadRatesStatus.onNext(true)
                }
            }, {
                downloadRatesStatus.onNext(false)
                it.printStackTrace()
            })
    }
}