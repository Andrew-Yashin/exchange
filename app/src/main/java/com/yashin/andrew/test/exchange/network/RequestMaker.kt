package com.yashin.andrew.test.exchange.network

import com.yashin.andrew.test.exchange.network.models.ExchangeResponse
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class RequestMaker @Inject constructor() {

    @Inject
    lateinit var api: Api

    fun getResponse(options: Map<String, String>): Observable<ExchangeResponse> {
        return api.getRequest(options)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
    }
}