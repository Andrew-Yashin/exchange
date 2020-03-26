package com.yashin.andrew.test.exchange.network

import com.yashin.andrew.test.exchange.data.RatesDatabase
import com.yashin.andrew.test.exchange.network.models.ExchangeResponse
import io.reactivex.Observable
import javax.inject.Inject

class DownloadRatesInteractor @Inject constructor() {

    @Inject
    lateinit var requestMaker: RequestMaker

    @Inject
    lateinit var ratesDatabase: RatesDatabase

    fun downloadAllRates(options: Map<String, String>?): Observable<ExchangeResponse> {
        return requestMaker.getResponse(options ?: mapOf(Pair("base", "USD")))
    }
}