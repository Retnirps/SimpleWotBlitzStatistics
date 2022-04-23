package com.majestaDev.blitzcalcsession.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TotalTanksStatistics(
    val totalAvgDamagePerSession: Long,
    val totalBattlesPerSession: Int,
    val totalPercentageOfWinsPerSession: Int
) : Parcelable