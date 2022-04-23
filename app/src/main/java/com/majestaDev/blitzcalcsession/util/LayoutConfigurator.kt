package com.majestaDev.blitzcalcsession.util

import android.content.Context
import androidx.core.content.ContextCompat
import com.majestaDev.blitzcalcsession.R

class LayoutConfigurator {

    companion object {
        fun getColorOfPercentageOfWins(context: Context, percentageOfWins: Int): Int {
            return when (percentageOfWins) {
                in 0..49 -> ContextCompat.getColor(context, android.R.color.black)
                in 50..59 -> ContextCompat.getColor(context, android.R.color.holo_green_dark)
                in 60..69 -> ContextCompat.getColor(context, R.color.teal_200)
                in 70..100 -> ContextCompat.getColor(context, R.color.purple_500)
                else -> ContextCompat.getColor(context, android.R.color.black)
            }
        }

        fun getColorOfNumberOfBattles(context: Context, numberOfBattles: Int): Int {
            return when (numberOfBattles) {
                in 1..3 -> ContextCompat.getColor(context, android.R.color.holo_red_dark)
                in 4..7 -> ContextCompat.getColor(context, android.R.color.holo_orange_dark)
                in 8..11 -> ContextCompat.getColor(context, R.color.yellow_500)
                in 12..15 -> ContextCompat.getColor(context, android.R.color.holo_green_dark)
                in 16..20 -> ContextCompat.getColor(context, R.color.teal_200)
                in 21..Int.MAX_VALUE -> ContextCompat.getColor(context, R.color.purple_500)
                else -> ContextCompat.getColor(context, android.R.color.black)
            }
        }

        fun getColorOfAvgDamage(context: Context, avgDamage: Long, tier: Int): Int {
            return when (tier) {
                in 5..10 -> getColorOfAvgDamageByTier(context, avgDamage, tier)
                else -> ContextCompat.getColor(context, android.R.color.black)
            }
        }

        private fun getColorOfAvgDamageByTier(context: Context, avgDamage: Long, tier: Int): Int {
            val range = getRangeByTier(tier)!!
            return when (avgDamage) {
                in range[0] until range[1] -> ContextCompat.getColor(context, android.R.color.holo_red_dark)
                in range[1] until range[2] -> ContextCompat.getColor(context, android.R.color.holo_orange_dark)
                in range[2] until range[3] -> ContextCompat.getColor(context, R.color.yellow_500)
                in range[3] until range[4] -> ContextCompat.getColor(context, android.R.color.holo_green_dark)
                in range[4] until range[5] -> ContextCompat.getColor(context, R.color.teal_200)
                in range[5] until Long.MAX_VALUE -> ContextCompat.getColor(context, R.color.purple_500)
                else -> ContextCompat.getColor(context, android.R.color.black)
            }
        }

        fun getColorOfNumberOfBattlesByAccount(context: Context, numberOfBattles: Int): Int {
            return when (numberOfBattles) {
                in 0..5000 -> ContextCompat.getColor(context, android.R.color.holo_red_dark)
                in 5000..7999 -> ContextCompat.getColor(context, android.R.color.holo_orange_dark)
                in 8000..9999 -> ContextCompat.getColor(context, R.color.yellow_500)
                in 10000..19999 -> ContextCompat.getColor(context, android.R.color.holo_green_dark)
                in 20000..29999 -> ContextCompat.getColor(context, R.color.teal_200)
                in 30000..Int.MAX_VALUE -> ContextCompat.getColor(context, R.color.purple_500)
                else -> ContextCompat.getColor(context, android.R.color.black)
            }
        }

        fun getColorOfAvgDamageByAccount(context: Context, avgDamage: Long): Int {
            return getColorOfAvgDamageByTier(context, avgDamage, 8)
        }

        private fun getRangeByTier(tier: Int): List<Long>? {
            val damageRangesByTier = mapOf<Int, List<Long>>(
                5 to listOf(0, 700, 800, 900, 1000, 1200),
                6 to listOf(0, 980, 1100, 1280, 1520, 1700),
                7 to listOf(0, 1160, 1300, 1510, 1790, 2000),
                8 to listOf(0, 1440, 1600, 1840, 2160, 2300),
                9 to listOf(0, 1620, 1800, 2070, 2430, 2700),
                10 to listOf(0, 1800, 2000, 2300, 2700, 3000)
            )

            return damageRangesByTier[tier]
        }

        fun getNationBackground(nation: String): Int {
            return when (nation) {
                "ussr" -> R.drawable.ussr
                "germany" -> R.drawable.german
                "usa" -> R.drawable.usa
                "china" -> R.drawable.chinese
                "other" -> R.drawable.hybrid
                "france" -> R.drawable.french
                "uk" -> R.drawable.uk
                "japan" -> R.drawable.japanese
                "european" -> R.drawable.european
                else -> R.drawable.transparent
            }
        }
    }
}