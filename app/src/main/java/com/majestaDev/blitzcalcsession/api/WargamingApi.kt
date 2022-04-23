package com.majestaDev.blitzcalcsession.api

import com.majestaDev.blitzcalcsession.BuildConfig
import com.majestaDev.blitzcalcsession.retrofit_model.Player
import com.majestaDev.blitzcalcsession.retrofit_model.PlayerPersonalData
import com.majestaDev.blitzcalcsession.retrofit_model.TankInfo
import com.majestaDev.blitzcalcsession.retrofit_model.TanksStatistics
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WargamingApi {
    @GET("account/list/?application_id=${BuildConfig.WOTB_API_KEY}&type=exact")
    suspend fun getPlayer(@Query(value="search", encoded=true) nickname: String): Response<Player>

    @GET("account/info/?application_id=${BuildConfig.WOTB_API_KEY}&fields=account_id%2C+nickname%2C+statistics.all.battles%2C+statistics.all.wins%2C+statistics.all.damage_dealt")
    suspend fun getPlayerPersonalData(@Query(value="account_id", encoded=true) accountId: Long): Response<PlayerPersonalData>

    @GET("tanks/stats/?application_id=${BuildConfig.WOTB_API_KEY}&fields=tank_id%2C+last_battle_time%2C+all.wins%2C+all.battles%2C+all.damage_dealt")
    suspend fun getTanksStatistics(@Query(value="account_id", encoded=true) accountId: Long): Response<TanksStatistics>

    @GET("encyclopedia/vehicles/?application_id=${BuildConfig.WOTB_API_KEY}&fields=name%2C+tier%2C+nation%2C+images.preview")
    suspend fun getTankInfo(@Query(value="tank_id", encoded=true) tankId: Long): Response<TankInfo>
}