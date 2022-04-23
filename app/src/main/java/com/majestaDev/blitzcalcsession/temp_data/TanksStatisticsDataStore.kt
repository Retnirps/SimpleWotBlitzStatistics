package com.majestaDev.blitzcalcsession.temp_data

import com.majestaDev.blitzcalcsession.model.*
import com.majestaDev.blitzcalcsession.retrofit_model.PlayerPersonalData
import com.majestaDev.blitzcalcsession.retrofit_model.TankStatistic
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class TanksStatisticsDataStore {
    var playerPersonalData: PlayerPersonalData? = null

    var timestamp: Long = 0
    var tanksStatisticsOnStop: List<TankStatistic>? = null
    var tanksStatisticsOnStart: List<TankStatistic>? = null

    private lateinit var tanksPlayedPerSession: ArrayList<TankStatistic>


    //    GET ACCOUNT INFO [
    private fun getNickname(): String? {
        return playerPersonalData?.data?.values?.toList()?.get(0)?.nickname
    }

    private fun getPercentageOfWins(): Int? {
        return playerPersonalData?.data?.values?.toList()?.get(0)?.statistics?.all?.battles?.let {
            playerPersonalData?.data?.values?.toList()?.get(0)?.statistics?.all?.wins?.toDouble()
                ?.div(it)?.times(100)?.roundToInt()
        }
    }

    private fun getNumberOfBattles(): Int? {
        return playerPersonalData?.data?.values?.toList()?.get(0)?.statistics?.all?.battles
    }

    private fun getAvgDamageDealt(): Long? {
        return playerPersonalData?.data?.values?.toList()?.get(0)?.statistics?.all?.battles?.let {
            playerPersonalData?.data?.values?.toList()?.get(0)?.statistics?.all?.damageDealt
                ?.toDouble()?.div(it)?.roundToLong()
        }
    }

    fun getAccountId(): Long? {
        return playerPersonalData?.data?.values?.toList()?.get(0)?.accountId
    }

    fun getPlayerShortInfo(): PlayerShortInfoDto? {
        val nickname = getNickname()
        val avgDamageDealt = getAvgDamageDealt()
        val percentageOfWins = getPercentageOfWins()
        val battles = getNumberOfBattles()

        return if (nickname != null
            && avgDamageDealt != null
            && percentageOfWins != null
            && battles != null
        ) {
            PlayerShortInfoDto(
                nickname,
                avgDamageDealt,
                percentageOfWins,
                battles
            )
        } else {
            null
        }
    }
//    ]


    //    GET TANKS PLAYED PER SESSION [
    private fun getTanksPlayedPerSession() {
        tanksPlayedPerSession = ArrayList()

        tanksStatisticsOnStop?.forEach {
            it.let {
                if (isTankPlayedPerSession(it)) {
                    tanksPlayedPerSession.add(it)
                }
            }
        }
    }

    private fun isTankPlayedPerSession(tankStatistic: TankStatistic): Boolean {
        if (tankStatistic.lastBattleTime > timestamp) {
            tanksStatisticsOnStart?.let {
                for (tank in it) {
                    if (tank.tankId == tankStatistic.tankId) {
                        return tankStatistic.all.battles != tank.all.battles
                    }
                }
            }

            return true
        }

        return false
    }
//    ]


    //    GET AVG STATISTICS PER SESSION ON EACH TANK [
    private fun getTanksStatistics(): List<TankStatisticDto> {
        val tanksStatistics = ArrayList<TankStatisticDto>()

        getTanksPlayedPerSession()
        if (tanksPlayedPerSession.size != 0) {
            for (tank in tanksPlayedPerSession) {
                if (tanksStatisticsOnStart != null) {
                    tanksStatisticsOnStart?.let {
                        for (tankOnStart in it) {
                            if (tank.tankId == tankOnStart.tankId) {
                                val battlesPerSession = tank.all.battles - tankOnStart.all.battles
                                val avgDamageDealtPerSession =
                                    (tank.all.damageDealt - tankOnStart.all.damageDealt) / battlesPerSession
                                val percentageOfWinsPerSession =
                                    (tank.all.wins - tankOnStart.all.wins).toDouble()
                                        .div(battlesPerSession)
                                        .times(100).roundToInt()
                                tanksStatistics.add(
                                    TankStatisticDto(
                                        tank.tankId,
                                        avgDamageDealtPerSession,
                                        battlesPerSession,
                                        percentageOfWinsPerSession
                                    )
                                )
                            }
                        }
                    }
                }
                if (tanksStatistics.isEmpty()) {
                    val percentageOfWinsPerSession = tank.all.wins.toDouble()
                        .div(tank.all.battles).times(100).roundToInt()

                    tanksStatistics.add(
                        TankStatisticDto(
                            tank.tankId,
                            tank.all.damageDealt,
                            tank.all.battles,
                            percentageOfWinsPerSession
                        )
                    )
                }
            }
        }

        return tanksStatistics
    }
//    ]


    //    GET TOTAL STATISTIC PER SESSION [
    private fun getTotalStatistic(): TotalTanksStatistics {
        val onStart = getSummaryOnStart()
        val onStop = getSummaryOnStop()

        var totalBattlesPerSession = 0
        var totalAvgDamagePerSession: Long = 0
        var totalPercentageOfWinsPerSession = 0

        if (onStart != onStop) {
            totalBattlesPerSession = onStop.totalBattles - onStart.totalBattles
            totalAvgDamagePerSession =
                (onStop.totalDamage - onStart.totalDamage) / totalBattlesPerSession
            totalPercentageOfWinsPerSession =
                (onStop.totalWins - onStart.totalWins).toDouble().div(totalBattlesPerSession)
                    .times(100)
                    .roundToInt()
        }

        return TotalTanksStatistics(
            totalAvgDamagePerSession,
            totalBattlesPerSession,
            totalPercentageOfWinsPerSession
        )
    }

    private fun getSummaryOnStart(): TotalStatisticsDto {
        var damageDealtSum: Long = 0
        var battlesSum = 0
        var winsSum = 0
        tanksStatisticsOnStart?.let {
            for (tank in it) {
                damageDealtSum += tank.all.damageDealt
                battlesSum += tank.all.battles
                winsSum += tank.all.wins
            }
        }
        return TotalStatisticsDto(damageDealtSum, battlesSum, winsSum)
    }

    private fun getSummaryOnStop(): TotalStatisticsDto {
        var damageDealtSum: Long = 0
        var battlesSum = 0
        var winsSum = 0
        tanksStatisticsOnStop?.let {
            for (tank in it) {
                damageDealtSum += tank.all.damageDealt
                battlesSum += tank.all.battles
                winsSum += tank.all.wins
            }
        }
        return TotalStatisticsDto(damageDealtSum, battlesSum, winsSum)
    }
//    ]


    //    GET FULL STATISTICS [
    fun getFullStatistics(): TanksStatisticsModel {
        val statistics = getTanksStatistics()
        val total = getTotalStatistic()

        return TanksStatisticsModel(timestamp, total, statistics)
    }
//    ]


    //    GET FAVOURITE TANK PER SESSION [
    fun getFavouriteTankPerSession(): TankStatisticDto {
        val tanks = getTanksStatistics()
        return tanks.maxByOrNull { it.battles }!!
    }
//    ]
}