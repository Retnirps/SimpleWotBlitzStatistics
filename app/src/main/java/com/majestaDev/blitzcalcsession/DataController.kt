package com.majestaDev.blitzcalcsession

import android.content.Context
import com.majestaDev.blitzcalcsession.models.Player
import com.majestaDev.blitzcalcsession.models.TankStatistics
import com.majestaDev.blitzcalcsession.models.Total

class DataController {
    var player: Player? = null
    var currentUnixTime: Long = 0
    var server: String = ""
    private var tanksStatisticsBefore = HashMap<Long, TankStatistics>()
    private var tanksStatisticsAfter = HashMap<Long, TankStatistics>()

    fun setPlayer(nickname: String) {
        player = RequestHandler().getPlayer(nickname, server)
    }

    private fun isPlayerValid(): Boolean {
        return player != null
    }

    fun getStartState(): Boolean {
        if (isPlayerValid()) {
            currentUnixTime = System.currentTimeMillis() / 1000L
            tanksStatisticsBefore = RequestHandler().getTanksStatistics(player!!.accountId, server)
            return true
        }

        return false
    }

    private fun getCurrentState() {
        tanksStatisticsAfter = RequestHandler().getTanksStatistics(player!!.accountId, server)
    }

    private fun getTanksPlayedPerSession(): HashMap<Long, TankStatistics> {
        val playedAfterPointInTime = HashMap<Long, TankStatistics>()

        tanksStatisticsAfter.forEach {
            if (isTankPlayedPerSession(it.value)) {
                playedAfterPointInTime[it.key] = it.value
            }
        }

        return playedAfterPointInTime
    }

    private fun isTankPlayedPerSession(tankStatistics: TankStatistics): Boolean {
        if (tankStatistics.lastBattleTime > currentUnixTime) {
            if (tanksStatisticsBefore.containsKey(tankStatistics.tankId)) {
                return tanksStatisticsBefore[tankStatistics.tankId]!!.battles != tankStatistics.battles
            }

            return true
        }

        return false
    }

    private fun getTankSessionStatistics(tankStatistics: TankStatistics): Triple<String, String, String> {
            val tankAvg = calcTankAvg(tankStatistics)

            val avgDamagePerSession = tankAvg.first
            val battlesPerSession = tankAvg.second
            val percentageOfWinsPerSession = tankAvg.third
            val tank = RequestHandler().getTank(tankStatistics.tankId, server)

            val tierLabels: Array<String> =
                arrayOf("I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X")

            return Triple("${tierLabels[tank.tier]} ${tank.name}",
                "avg damage: $avgDamagePerSession\nbattles played: $battlesPerSession\n% of wins: $percentageOfWinsPerSession%",
                tank.previewImageUrl)
    }

    private fun isTankNotNew(tankId: Long): Boolean {
        return tanksStatisticsBefore.containsKey(tankId)
    }

    private fun calcTankAvg(tankStatistics: TankStatistics): Triple<Int, Int, Int> {
        val tankStatisticsPerSession = getTankStatisticsPerSession(tankStatistics)

        val damageDealtPerSession = tankStatisticsPerSession.first
        val winsPerSession = tankStatisticsPerSession.second
        val battlesPerSession = tankStatisticsPerSession.third
        val avgDamagePerSession = damageDealtPerSession / battlesPerSession
        val percentageOfWinsPerSession =
            (winsPerSession.toFloat() / battlesPerSession * 100).toInt()

        return Triple(avgDamagePerSession, battlesPerSession, percentageOfWinsPerSession)
    }

    private fun getTankStatisticsPerSession(tankStatistics: TankStatistics): Triple<Int, Int, Int> {
        val damageDealtPerSession: Int
        val winsPerSession: Int
        val battlesPerSession: Int

        if (isTankNotNew(tankStatistics.tankId)) {
            damageDealtPerSession =
                tankStatistics.damageDealt - tanksStatisticsBefore[tankStatistics.tankId]!!.damageDealt
            winsPerSession =
                tankStatistics.wins - tanksStatisticsBefore[tankStatistics.tankId]!!.wins
            battlesPerSession =
                tankStatistics.battles - tanksStatisticsBefore[tankStatistics.tankId]!!.battles
        } else {
            damageDealtPerSession = tankStatistics.damageDealt
            winsPerSession = tankStatistics.wins
            battlesPerSession = tankStatistics.battles
        }

        return Triple(damageDealtPerSession, winsPerSession, battlesPerSession)
    }

    private fun getTotalStatistics(): Total {
        return RequestHandler().getTotal(player!!.accountId)
    }

    private fun getTotalStatisticsPerSession(totalOnStart: Total, totalOnStop: Total): Triple<Int, Int, Int> {
        val avgDamagePerSession: Int =
            (totalOnStop.damageDealt - totalOnStart.damageDealt) / (totalOnStop.battles - totalOnStart.battles)
        val percentageOfWinsPerSession: Int =
            (totalOnStop.wins - totalOnStart.wins) / (totalOnStop.battles - totalOnStart.battles)
        val battlesPerSession: Int =
            totalOnStop.battles - totalOnStart.battles

        return Triple(avgDamagePerSession, percentageOfWinsPerSession, battlesPerSession)
    }

    fun stopSession(): ArrayList<Triple<String, String, String>> {
        getCurrentState()
        val tanksPlayedPerSession = getTanksPlayedPerSession()
        val dataToShow = ArrayList<Triple<String, String, String>>()

        tanksPlayedPerSession.forEach {
            dataToShow.add(getTankSessionStatistics(it.value))
        }

        return dataToShow
    }

    fun saveString(key: String, value: String, context: Context) {
        val preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, context: Context): String {
        val preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getString(key, "").toString()
    }
}