package com.majestaDev.blitzcalcsession.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface TanksStatisticsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTanksStatistics(tanksStatisticsData: TanksStatisticsData)

    @Query("select * from tanks_statistics_table group by timestamp order by id asc")
    fun getAllTanksStatistics(): LiveData<List<TanksStatisticsData>>

    @Query("delete from tanks_statistics_table")
    suspend fun deleteAllStatistics()

    @Delete
    suspend fun deleteTanksStatistics(tanksStatisticsData: TanksStatisticsData)
}