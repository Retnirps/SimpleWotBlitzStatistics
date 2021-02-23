package com.majestaDev.blitzcalcsession.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [TanksStatisticsData::class], version = 1, exportSchema = false)
abstract class TanksStatisticsDatabase: RoomDatabase() {
    abstract fun tanksStatisticsDao(): TanksStatisticsDao

    companion object {
        @Volatile
        private var INSTANCE: TanksStatisticsDatabase? = null

        fun getDatabase(context: Context): TanksStatisticsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TanksStatisticsDatabase::class.java,
                    "tanks_statistics_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}