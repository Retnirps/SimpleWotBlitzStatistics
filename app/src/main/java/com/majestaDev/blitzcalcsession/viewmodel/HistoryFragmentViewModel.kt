package com.majestaDev.blitzcalcsession.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.majestaDev.blitzcalcsession.data.TanksStatisticsDatabase
import com.majestaDev.blitzcalcsession.db_model.SessionInfo
import com.majestaDev.blitzcalcsession.repository.TanksStatisticsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HistoryFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val tanksStatisticsRepository = TanksStatisticsRepository(TanksStatisticsDatabase.getDatabase(application).tanksStatisticsDao())
    val sessions = tanksStatisticsRepository.getSessionsInfoWithTanksStatistics
    fun searchByDate(searchQuery: String) = tanksStatisticsRepository.searchByDate(searchQuery)
    fun getTankStatisticWithSessionInfoFilteredByName(name: String) = tanksStatisticsRepository.getTankStatisticWithSessionInfoFilteredByName(name)

    fun deleteStatistics(sessionInfo: SessionInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            tanksStatisticsRepository.deleteStatistics(sessionInfo)
        }
    }
}