package com.majestaDev.blitzcalcsession.models
import com.google.gson.JsonElement

class Player(playerInfo: JsonElement) {
    var nickname: String = playerInfo.asJsonObject.get("nickname").asString
    var accountId: Long = playerInfo.asJsonObject.get("account_id").asLong
}