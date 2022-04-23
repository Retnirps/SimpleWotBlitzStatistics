package com.majestaDev.blitzcalcsession.model

import android.os.Parcelable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TankAvgStatisticsPerSession(
    val avgDamageDealtPerSession: Long,
    val battlesPerSession: Int,
    val percentageOfWinsPerSession: Int,
    val tier: Int,
    val name: String,
    val nation: String,
    private val _urlPreview: String
) : Parcelable {
    @IgnoredOnParcel
    var urlPreview = _urlPreview
        get() = field.replace("http:", "https:")
    @IgnoredOnParcel
    var tierRoman = when(tier) {
        1 -> "I"
        2 -> "II"
        3 -> "III"
        4 -> "IV"
        5 -> "V"
        6 -> "VI"
        7 -> "VII"
        8 -> "VIII"
        9 -> "IX"
        10 -> "X"
        else -> "?"
    }
}