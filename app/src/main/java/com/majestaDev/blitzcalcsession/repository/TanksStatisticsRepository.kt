package com.majestaDev.blitzcalcsession.repository

import com.majestaDev.blitzcalcsession.data.TanksStatisticsDao
import com.majestaDev.blitzcalcsession.db_model.SessionInfo
import com.majestaDev.blitzcalcsession.db_model.TankStatistic
import com.majestaDev.blitzcalcsession.model.TanksStatisticsModelFinal

class TanksStatisticsRepository(private val tanksStatisticsDao: TanksStatisticsDao) {
    val getSessionsInfoWithTanksStatistics = tanksStatisticsDao.getSessionInfoWithTanksStatistics()
    fun searchByDate(searchQuery: String) = tanksStatisticsDao.searchByDate(searchQuery)
    fun getTankStatisticWithSessionInfoFilteredByName(name: String) =
        tanksStatisticsDao.getTankStatisticWithSessionInfoFilteredByName(name)

    suspend fun addStatistics(tanksStatisticsModelFinal: TanksStatisticsModelFinal) {
        val sessionInfo = tanksStatisticsModelFinal.total?.let {
            SessionInfo(0, tanksStatisticsModelFinal.date, tanksStatisticsModelFinal.time, it)
        }

        var infoId: Long = 0
        if (sessionInfo != null) {
            infoId = tanksStatisticsDao.addSessionInfo(sessionInfo)
        }

        tanksStatisticsModelFinal.listOfTanks.forEach {
            val tankStatistic = it?.let {
                TankStatistic(0, infoId, it)
            }
            if (tankStatistic != null) {
                tanksStatisticsDao.addTankStatistic(tankStatistic)
            }
        }
    }

    suspend fun deleteStatistics(sessionInfo: SessionInfo) {
        tanksStatisticsDao.deleteStatistics(sessionInfo)
    }

//    suspend fun updateTankStatistic(
//        statisticsId: Long,
//        name: String,
//        tier: Int,
//        nation: String,
//        urlPreview: String
//    ) = tanksStatisticsDao.updateTankStatistic(statisticsId, name, tier, nation, urlPreview)
}