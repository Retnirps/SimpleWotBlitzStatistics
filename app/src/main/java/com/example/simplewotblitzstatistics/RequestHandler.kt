package com.example.simplewotblitzstatistics

import com.example.simplewotblitzstatistics.models.Player
import com.example.simplewotblitzstatistics.models.Tank
import com.example.simplewotblitzstatistics.models.TankStatistics
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.JsonNull
import com.google.gson.JsonParser.parseString

class RequestHandler {
    private var data = ""

    fun getPlayer(nickname: String): Player? {
        val url = "https://api.wotblitz.ru/wotb/account/list/?application_id=1d3f0909466385a60229b0cb760146b5&search=$nickname&type=exact"
        val response = getResponse(url)

        val jsonObject = parseString(response).asJsonObject
        val data = jsonObject.get("data").asJsonArray
        try {
            if (data.size() == 0) {
                throw NoPlayerException("no player with this nickname")
            }
        } catch (e: NoPlayerException) {
            return null
        }
        val playerInfo = data.get(0)

        return Player(playerInfo)
    }

    fun getTanksStatistics(accountId: Long): HashMap<Long, TankStatistics> {
        val url = "https://api.wotblitz.ru/wotb/tanks/stats/?application_id=1d3f0909466385a60229b0cb760146b5&account_id=$accountId&fields=last_battle_time%2C+tank_id%2C+all.battles%2C+all.wins%2C+all.damage_dealt"
        val response = getResponse(url)

        val jsonObject = parseString(response).asJsonObject
        var playerTanksStatisticsInfo = jsonObject.get("data")
            .asJsonObject
            .get(accountId.toString())

        val tanksStatistics = HashMap<Long, TankStatistics>()

        if (playerTanksStatisticsInfo != JsonNull()) {
            playerTanksStatisticsInfo = playerTanksStatisticsInfo.asJsonArray
            for (i in 0 until playerTanksStatisticsInfo.size()) {
                val tankStatistics = TankStatistics(playerTanksStatisticsInfo.get(i))
                tanksStatistics[tankStatistics.tankId] = tankStatistics
            }
        }

        return tanksStatistics
    }

    fun getTank(tankId: Long): Tank {
        val url = "https://api.wotblitz.ru/wotb/encyclopedia/vehicles/?application_id=1d3f0909466385a60229b0cb760146b5&tank_id=$tankId&fields=tier%2C+name%2C+images.preview"
        val response = getResponse(url)

        val jsonObject = parseString(response).asJsonObject

        val data = jsonObject.get("data")
            .asJsonObject
            .get(tankId.toString())

        return Tank(data)
    }

    private fun getResponse(url: String): String {
        val httpAsync = url
                .httpGet()
                .responseString { request, response, result ->
                    data = when (result) {
                        is Result.Failure -> {
                            result.getException().toString()
                        }
                        is Result.Success -> {
                            result.get()
                        }
                    }
                }
        httpAsync.join()
        return data
    }
}