package com.example.simplewotblitzstatistics.data

import androidx.lifecycle.LiveData

class TanksStatisticsRepository(private val tanksStatisticsDao: TanksStatisticsDao) {
    val getAllTanksStatistics: LiveData<List<TanksStatisticsData>> = tanksStatisticsDao.getAllTanksStatistics()
    suspend fun addTanksStatistics(tanksStatisticsData: TanksStatisticsData) {
        tanksStatisticsDao.addTanksStatistics(tanksStatisticsData)
    }
}