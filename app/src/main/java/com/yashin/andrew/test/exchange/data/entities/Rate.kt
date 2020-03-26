package com.yashin.andrew.test.exchange.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonProperty

@Entity
data class Rate (
    @PrimaryKey
    @JsonProperty("name")
    var name: String,

    @JsonProperty("course")
    var course: Float
)