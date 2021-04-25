package com.majestaDev.blitzcalcsession.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TanksStatisticsViewModel(application: Application): AndroidViewModel(application) {
    val getAllTanksStatistics: LiveData<List<TanksStatisticsData>>
    private val repository: TanksStatisticsRepository

    init {
        val tanksStatisticsDao = TanksStatisticsDatabase.getDatabase(application).tanksStatisticsDao()
        repository = TanksStatisticsRepository(tanksStatisticsDao)
        getAllTanksStatistics = repository.getAllTanksStatistics
    }

    fun addTanksStatistics(tanksStatisticsData: TanksStatisticsData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addTanksStatistics(tanksStatisticsData)
        }
    }

    fun deleteAllStatistics() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllStatistics()
        }
    }

    fun deleteTanksStatistics(tanksStatisticsData: TanksStatisticsData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteTanksStatistics(tanksStatisticsData)
        }
    }
}