package com.majestaDev.blitzcalcsession.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.majestaDev.blitzcalcsession.constant.Constants

class SharedPreferencesRepository(application: Application) {
    private val preferences: SharedPreferences =
        application.getSharedPreferences(Constants.PREFERENCES_NAME, Context.MODE_PRIVATE)

    var nickname: String
        get() = preferences.getString(Constants.NICKNAME, "") ?: ""
        set(nickname) = preferences.edit().putString(Constants.NICKNAME, nickname).apply()

    var region: String
        get() = preferences.getString(Constants.REGION, "") ?: ""
        set(region) = preferences.edit().putString(Constants.REGION, region).apply()

    var timestampOnStart: Long
        get() = preferences.getLong(Constants.TIMESTAMP_ON_START, 0)
        set(timestamp) = preferences.edit().putLong(Constants.TIMESTAMP_ON_START, timestamp).apply()

    var savedStatisticsState: String
        get() = preferences.getString(Constants.SAVED_STATISTICS_STATE, "") ?: ""
        set(state) = preferences.edit().putString(Constants.SAVED_STATISTICS_STATE, state).apply()
}