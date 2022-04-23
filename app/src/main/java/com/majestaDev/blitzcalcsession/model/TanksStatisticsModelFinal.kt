package com.majestaDev.blitzcalcsession.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TanksStatisticsModelFinal(
    val date: String,
    val time: String,
    val total: TotalTanksStatistics?,
    val listOfTanks: List<TankAvgStatisticsPerSession?>
) : Parcelable