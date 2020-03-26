package com.yashin.andrew.test.exchange.network.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ExchangeResponse(
    @JsonProperty("rates")
    val rates: Map<String, Float>,

    @JsonProperty("base")
    val base: String,

    @JsonProperty("date")
    val date: Date
)