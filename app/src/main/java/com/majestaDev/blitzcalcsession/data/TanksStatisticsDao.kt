package com.majestaDev.blitzcalcsession.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.majestaDev.blitzcalcsession.db_model.*

@Dao
interface TanksStatisticsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addSessionInfo(sessionInfo: SessionInfo): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTankStatistic(tankStatistic: TankStatistic)

    @Transaction
    @Query("select * from session_info order by infoId asc")
    fun getSessionInfoWithTanksStatistics(): LiveData<List<SessionInfoWithTanksStatistics>>

    @Delete
    suspend fun deleteStatistics(sessionInfo: SessionInfo)

    @Transaction
    @Query("select * from session_info where date like :searchQuery order by infoId asc")
    fun searchByDate(searchQuery: String): LiveData<List<SessionInfoWithTanksStatistics>>

    @Transaction
    @Query("select * from tank_statistic where name like :name order by tier asc")
    fun getTankStatisticWithSessionInfoFilteredByName(name: String): LiveData<List<TankStatisticWithSessionInfo>>

//    @Query("update tank_statistic set name = :name, tier = :tier, nation = :nation, _urlPreview = :urlPreview where statisticsId = :statisticsId")
//    suspend fun updateTankStatistic(statisticsId: Long, name: String, tier: Int, nation: String, urlPreview: String)
}