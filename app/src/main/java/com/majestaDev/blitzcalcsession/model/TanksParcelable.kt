package com.majestaDev.blitzcalcsession.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TanksParcelable(
    val statisticsId: Long,
    val tankAvgStatisticsPerSession: TankAvgStatisticsPerSession
) : Parcelable
