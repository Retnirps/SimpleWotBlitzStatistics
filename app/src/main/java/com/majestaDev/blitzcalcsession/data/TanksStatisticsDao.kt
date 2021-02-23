package com.majestaDev.blitzcalcsession.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TanksStatisticsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTanksStatistics(tanksStatisticsData: TanksStatisticsData)

    @Query("select * from tanks_statistics_table group by timestamp order by id asc")
    fun getAllTanksStatistics(): LiveData<List<TanksStatisticsData>>

    @Query("delete from tanks_statistics_table")
    fun deleteAllStatistics()
}