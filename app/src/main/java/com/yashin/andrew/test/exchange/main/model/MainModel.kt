package com.yashin.andrew.test.exchange.main.model

import com.yashin.andrew.test.exchange.data.RatesDatabase
import com.yashin.andrew.test.exchange.data.entities.Rate
import com.yashin.andrew.test.exchange.network.RequestMaker
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class MainModel @Inject constructor() {

    @Inject
    lateinit var requestMaker: RequestMaker

    @Inject
    lateinit var ratesDatabase: RatesDatabase

    fun getRate(name: String): Single<Rate> {
        return ratesDatabase.rateDao().getByName(name)
    }

    fun getCount(): Single<Int> {
        return ratesDatabase.rateDao().getCount()
    }

    fun downloadAndSaveRates(): Observable<Int> {
        return  requestMaker.getResponse(mapOf(Pair("base","USD")))
            .map{
                ratesDatabase.runInTransaction {
                    ratesDatabase.rateDao().deleteAll()
                    it.rates.forEach {
                        ratesDatabase.rateDao().insert(Rate(it.key, it.value))
                    }
                }
                return@map it.rates.size
            }
    }
}