package com.majestaDev.blitzcalcsession.model

data class TanksStatisticsModel(
    val timestamp: Long,
    val total: TotalTanksStatistics?,
    val listOfTanks: List<TankStatisticDto>
)