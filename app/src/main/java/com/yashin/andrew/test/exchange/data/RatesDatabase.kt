package com.yashin.andrew.test.exchange.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.yashin.andrew.test.exchange.data.dao.RateDao
import com.yashin.andrew.test.exchange.data.entities.Rate

@Database(entities = [Rate::class], version = 1)
abstract class RatesDatabase : RoomDatabase() {
    abstract fun rateDao(): RateDao

    companion object{
        fun create(context: Context): RatesDatabase{
            return Room.databaseBuilder(
                context,
                RatesDatabase::class.java,"database"
            )
                .build()
        }
    }
}