package com.yashin.andrew.test.exchange.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yashin.andrew.test.exchange.data.entities.Rate
import io.reactivex.Single

@Dao
interface RateDao {
    @Insert
    fun insert(rate: Rate)

    @Query("DELETE FROM rate")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM rate")
    fun getCount(): Single<Int>

    @Query("SELECT * FROM rate WHERE name = :name")
    fun getByName(name: String): Single<Rate>
}