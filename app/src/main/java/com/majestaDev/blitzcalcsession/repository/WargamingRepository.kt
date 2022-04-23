package com.majestaDev.blitzcalcsession.repository

import com.majestaDev.blitzcalcsession.retrofit_model.Player
import com.majestaDev.blitzcalcsession.retrofit_model.PlayerPersonalData
import com.majestaDev.blitzcalcsession.retrofit_model.TankInfo
import com.majestaDev.blitzcalcsession.retrofit_model.TanksStatistics
import com.majestaDev.blitzcalcsession.util.RetrofitInstance
import retrofit2.Response

class WargamingRepository(region: String) {

    private var retrofit = RetrofitInstance(region)

    suspend fun getPlayer(nickname: String): Response<Player> {
        return retrofit.api.getPlayer(nickname)
    }

    suspend fun getPlayerPersonalData(accountId: Long): Response<PlayerPersonalData> {
        return retrofit.api.getPlayerPersonalData(accountId)
    }

    suspend fun getTanksStatistics(accountId: Long): Response<TanksStatistics> {
        return retrofit.api.getTanksStatistics(accountId)
    }

    suspend fun getTankInfo(tankId: Long): Response<TankInfo> {
        return retrofit.api.getTankInfo(tankId)
    }
}