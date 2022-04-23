package com.majestaDev.blitzcalcsession.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StatisticsParcelable(
    val date: String,
    val time: String,
    val totalTanksStatistics: TotalTanksStatistics,
    val tanks: List<TanksParcelable>
) : Parcelable
