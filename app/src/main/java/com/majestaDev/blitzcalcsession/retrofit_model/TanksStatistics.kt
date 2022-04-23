package com.majestaDev.blitzcalcsession.retrofit_model

import com.google.gson.annotations.SerializedName

data class TanksStatistics(
    val data:  Map<String, List<TankStatistic>>
)

data class TankStatistic(
    val all: StatisticsOnTank,
    @SerializedName("last_battle_time")
    val lastBattleTime: Long,
    @SerializedName("tank_id")
    val tankId: Long
)

data class StatisticsOnTank(
    @SerializedName("damage_dealt")
    val damageDealt: Long,
    val wins: Int,
    val battles: Int
)
