package com.majestaDev.blitzcalcsession.model

data class TankStatisticWithSessionInfoModel(
    val date: String,
    val time: String,
    val statistic: TankAvgStatisticsPerSession
) : IListItem
