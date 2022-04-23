package com.majestaDev.blitzcalcsession.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.majestaDev.blitzcalcsession.data.TanksStatisticsDatabase
import com.majestaDev.blitzcalcsession.repository.SharedPreferencesRepository
import com.majestaDev.blitzcalcsession.repository.TanksStatisticsRepository
import com.majestaDev.blitzcalcsession.repository.WargamingRepository
import com.majestaDev.blitzcalcsession.retrofit_model.TankInfoValue

class SessionDetailsFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferencesRepository = SharedPreferencesRepository(application)
    private val wargamingRepository = WargamingRepository(getRegion())
    private val tanksStatisticsRepository = TanksStatisticsRepository(
        TanksStatisticsDatabase.getDatabase(application).tanksStatisticsDao()
    )

    private fun getRegion(): String {
        return sharedPreferencesRepository.region
    }

    suspend fun getTankInfo(unknownTankName: String): TankInfoValue? {
        val tankId = unknownTankName.substring(
            unknownTankName.indexOf("(") + 1,
            unknownTankName.indexOf(")")
        ).toLong()

        return getTankInfoResponse(tankId)
    }

    private suspend fun getTankInfoResponse(tankId: Long): TankInfoValue? {
        val response = wargamingRepository.getTankInfo(tankId)
        if (response.isSuccessful) {
            return response.body()?.data?.values?.toList()?.get(0)
        }

        return null
    }

//    suspend fun updateTankStatistic(
//        statisticsId: Long,
//        name: String,
//        tier: Int,
//        nation: String,
//        urlPreview: String
//    ) = tanksStatisticsRepository.updateTankStatistic(
//        statisticsId,
//        name,
//        tier,
//        nation,
//        urlPreview
//    )
}