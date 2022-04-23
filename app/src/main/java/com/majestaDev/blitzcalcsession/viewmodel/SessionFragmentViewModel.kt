package com.majestaDev.blitzcalcsession.viewmodel

import android.annotation.SuppressLint
import android.app.Application
import android.app.Person
import androidx.lifecycle.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.majestaDev.blitzcalcsession.data.TanksStatisticsDatabase
import com.majestaDev.blitzcalcsession.model.PlayerShortInfoDto
import com.majestaDev.blitzcalcsession.model.TankAvgStatisticsPerSession
import com.majestaDev.blitzcalcsession.model.TanksStatisticsModelFinal
import com.majestaDev.blitzcalcsession.repository.SharedPreferencesRepository
import com.majestaDev.blitzcalcsession.repository.TanksStatisticsRepository
import com.majestaDev.blitzcalcsession.repository.WargamingRepository
import com.majestaDev.blitzcalcsession.retrofit_model.PlayerPersonalData
import com.majestaDev.blitzcalcsession.retrofit_model.TankInfoValue
import com.majestaDev.blitzcalcsession.retrofit_model.TankStatistic
import com.majestaDev.blitzcalcsession.temp_data.TanksStatisticsDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SessionFragmentViewModel(application: Application) : AndroidViewModel(application) {
    private val tanksStatisticsRepository = TanksStatisticsRepository(TanksStatisticsDatabase.getDatabase(application).tanksStatisticsDao())
    private lateinit var wargamingRepository: WargamingRepository
    private val sharedPreferencesRepository = SharedPreferencesRepository(application)
    private val tanksStatisticsDataStore = TanksStatisticsDataStore()
    var hasSavedStatistics = false

    init {
//        if (savedStateHandle.contains(Constants.SAVED_STATISTICS_STATE)) {
//            hasSavedStatistics = true
//            val jsonStatistics = savedStateHandle[Constants.SAVED_STATISTICS_STATE] ?: ""
//            val savedTanksStatisticsOnStart: List<TankStatistic> =
//                Gson().fromJson(jsonStatistics, object : TypeToken<List<TankStatistic>>() {}.type) ?: emptyList()
//            tanksStatisticsDataStore.timestamp = savedStateHandle[Constants.TIMESTAMP_ON_START] ?: 0
//            tanksStatisticsDataStore.tanksStatisticsOnStart = savedTanksStatisticsOnStart
//        }

        if (sharedPreferencesRepository.savedStatisticsState.isNotEmpty()) {
            hasSavedStatistics = true

            val jsonStatistics = sharedPreferencesRepository.savedStatisticsState
            val savedTanksStatisticsOnStart: List<TankStatistic> =
                Gson().fromJson(jsonStatistics, object : TypeToken<List<TankStatistic>>() {}.type) ?: emptyList()

            tanksStatisticsDataStore.timestamp = sharedPreferencesRepository.timestampOnStart
            tanksStatisticsDataStore.tanksStatisticsOnStart = savedTanksStatisticsOnStart
        }
    }

    private val _playerPersonalData = MutableLiveData<PlayerShortInfoDto?>()
    val playerPersonalData: LiveData<PlayerShortInfoDto?>
        get() = _playerPersonalData

    private val _tanksStatistics = MutableLiveData<TanksStatisticsModelFinal>()
    val tanksStatistics: LiveData<TanksStatisticsModelFinal>
        get() = _tanksStatistics

    private val _favouriteTank = MutableLiveData<TankAvgStatisticsPerSession>()
    val favouriteTank: LiveData<TankAvgStatisticsPerSession>
        get() = _favouriteTank

    private fun initializeRepository() {
        wargamingRepository = WargamingRepository(getRegion())
    }

    fun getNickname(): String {
        return sharedPreferencesRepository.nickname
    }

    fun setNickname(nickname: String) {
        sharedPreferencesRepository.nickname = nickname
    }

    fun getRegion(): String {
        return sharedPreferencesRepository.region
    }

    fun setRegion(region: String) {
        sharedPreferencesRepository.region = region
    }

    fun getPlayerPersonalData(nickname: String) {
        initializeRepository()
        viewModelScope.launch(Dispatchers.IO) {
            getPlayerPersonalDataResponse(nickname)
        }
    }

    private suspend fun getPlayerPersonalDataResponse(nickname: String) {
        val response = wargamingRepository.getPlayer(nickname)
        if (response.isSuccessful && response.body()?.data?.isNotEmpty() == true) {
            response.body()?.data?.get(0)?.let {
                val personalData = wargamingRepository.getPlayerPersonalData(it.accountId)
                if (personalData.isSuccessful) {
                    tanksStatisticsDataStore.playerPersonalData = personalData.body()

                    _playerPersonalData.postValue(tanksStatisticsDataStore.getPlayerShortInfo())
                }
            }
        } else {
            _playerPersonalData.postValue(null)
        }
    }

    fun getTanksStatisticsOnStart() {
        viewModelScope.launch(Dispatchers.IO) {
            tanksStatisticsDataStore.getAccountId()?.let {
                val response = wargamingRepository.getTanksStatistics(it)
                if (response.isSuccessful) {
                    tanksStatisticsDataStore.timestamp = System.currentTimeMillis() / 1000L

                    val statistics = response.body()?.data?.values?.toList()?.get(0)
                    tanksStatisticsDataStore.tanksStatisticsOnStart = statistics

//                    savedStateHandle[Constants.TIMESTAMP_ON_START] = tanksStatisticsDataStore.timestamp
                    val jsonStatisticsOnStart = Gson().toJson(tanksStatisticsDataStore.tanksStatisticsOnStart)
//                    savedStateHandle[Constants.SAVED_STATISTICS_STATE] = jsonStatisticsOnStart

                    sharedPreferencesRepository.timestampOnStart = tanksStatisticsDataStore.timestamp
                    sharedPreferencesRepository.savedStatisticsState = jsonStatisticsOnStart
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    fun getTanksStatisticsOnStop() {
        viewModelScope.launch(Dispatchers.IO) {
            tanksStatisticsDataStore.getAccountId()?.let {
                val response = wargamingRepository.getTanksStatistics(it)
                if (response.isSuccessful) {
                    val statistics = response.body()?.data?.values?.toList()?.get(0)
                    tanksStatisticsDataStore.tanksStatisticsOnStop = statistics

                    val fullStatistics = tanksStatisticsDataStore.getFullStatistics()

                    if (fullStatistics.listOfTanks.isNotEmpty()) {
                        val listOfTanks = fullStatistics.listOfTanks.map { tank ->
                            val tankInfo = getTankInfo(tank.tankId)
                            tankInfo?.let { info ->
                                TankAvgStatisticsPerSession(
                                    tank.damageDealt,
                                    tank.battles,
                                    tank.percentageOfWins,
                                    info.tier,
                                    info.name,
                                    info.nation,
                                    info.images.preview
                                )
                            } ?: run {
                                TankAvgStatisticsPerSession(
                                    tank.damageDealt,
                                    tank.battles,
                                    tank.percentageOfWins,
                                    0,
                                    "Tank(${tank.tankId})",
                                    "Unknown",
                                    ""
                                )
                            }
                        }

                        val sdfDate = java.text.SimpleDateFormat("MMM d")
                        val sdfTime = java.text.SimpleDateFormat("HH:mm")
                        val timeAndDate = java.util.Date(fullStatistics.timestamp * 1000)
                        val sessionDate = sdfDate.format(timeAndDate)
                        val sessionTime = sdfTime.format(timeAndDate)

                        val result = TanksStatisticsModelFinal(
                            sessionDate,
                            sessionTime,
                            fullStatistics.total,
                            listOfTanks
                        )
                        _tanksStatistics.postValue(result)

                        getFavouriteTankPerSession()
                    }
                }
            }
        }
    }

    private suspend fun getFavouriteTankPerSession() {
        val favouriteTank = tanksStatisticsDataStore.getFavouriteTankPerSession()
        val tankInfo = getTankInfo(favouriteTank.tankId)
        _favouriteTank.postValue(
            tankInfo?.let { info ->
                TankAvgStatisticsPerSession(
                    favouriteTank.damageDealt,
                    favouriteTank.battles,
                    favouriteTank.percentageOfWins,
                    info.tier,
                    info.name,
                    info.nation,
                    info.images.preview
                )
            } ?: run {
                TankAvgStatisticsPerSession(
                    favouriteTank.damageDealt,
                    favouriteTank.battles,
                    favouriteTank.percentageOfWins,
                    0,
                    "Tank(${favouriteTank.tankId})",
                    "Unknown",
                    ""
                )
            }
        )
    }

    private suspend fun getTankInfo(tankId: Long): TankInfoValue? {
        val response = wargamingRepository.getTankInfo(tankId)
        if (response.isSuccessful) {
            return response.body()?.data?.values?.toList()?.get(0)
        }

        return null
    }

    fun addTanksStatistics(tanksStatisticsModelFinal: TanksStatisticsModelFinal) {
        viewModelScope.launch(Dispatchers.IO) {
            tanksStatisticsRepository.addStatistics(tanksStatisticsModelFinal)
        }
    }

    fun clearSavedStatistics() {
        hasSavedStatistics = false
//        savedStateHandle.remove<String>(Constants.SAVED_STATISTICS_STATE)
        sharedPreferencesRepository.savedStatisticsState = ""
    }
}