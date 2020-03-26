package com.yashin.andrew.test.exchange.network

import com.yashin.andrew.test.exchange.network.models.ExchangeResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface Api {
    @GET("/latest")
    fun getRequest(@QueryMap options: Map<String, String>): Observable<ExchangeResponse>
}