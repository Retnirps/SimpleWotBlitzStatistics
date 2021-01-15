package com.example.simplewotblitzstatistics.models

import com.google.gson.JsonElement

class Tank(tankInfo: JsonElement) {
    var tier = tankInfo.asJsonObject.get("tier").asInt
        get() = field - 1
    var name: String = tankInfo.asJsonObject.get("name").asString
    var previewImageUrl: String =
        tankInfo.asJsonObject.get("images").asJsonObject.get("preview").asString
        get() {
            return field.replace("http", "https")
        }
}