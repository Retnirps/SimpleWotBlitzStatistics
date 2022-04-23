package com.majestaDev.blitzcalcsession.util

import com.majestaDev.blitzcalcsession.db_model.SessionInfo
import com.majestaDev.blitzcalcsession.db_model.SessionInfoWithTanksStatistics
import com.majestaDev.blitzcalcsession.db_model.TankStatistic
import com.majestaDev.blitzcalcsession.db_model.TankStatisticWithSessionInfo
import com.majestaDev.blitzcalcsession.model.*

class Converter {
    companion object {
        fun List<TanksParcelable>.toListOfTankAvgStatisticsPerSession(): List<TankAvgStatisticsPerSession> {
            return map {
                val image = it.tankAvgStatisticsPerSession.urlPreview

                return@map TankAvgStatisticsPerSession(
                    it.tankAvgStatisticsPerSession.avgDamageDealtPerSession,
                    it.tankAvgStatisticsPerSession.battlesPerSession,
                    it.tankAvgStatisticsPerSession.percentageOfWinsPerSession,
                    it.tankAvgStatisticsPerSession.tier,
                    it.tankAvgStatisticsPerSession.name,
                    it.tankAvgStatisticsPerSession.nation,
                    image
                )
            }
        }

        fun SessionInfoWithTanksStatistics.toStatisticsParcelable() =
            StatisticsParcelable(
                sessionInfo.date,
                sessionInfo.time,
                sessionInfo.totalSessionStatistic,
                statistics.map {
                    TanksParcelable(
                        it.statisticsId,
                        it.statistic
                    )
                }
            )

        fun TanksStatisticsModelFinal.toStatisticsParcelable() =
            StatisticsParcelable(
                date,
                time,
                total!!,
                listOfTanks.map {
                    TanksParcelable(
                        0,
                        TankAvgStatisticsPerSession(
                            it!!.avgDamageDealtPerSession,
                            it.battlesPerSession,
                            it.percentageOfWinsPerSession,
                            it.tier,
                            it.name,
                            it.nation,
                            it.urlPreview
                        )
                    )
                }
            )

        fun convertToListOfTankStatisticWithSessionInfoModel(listOfTankStatisticWithSessionInfo: List<TankStatisticWithSessionInfo>): ArrayList<TankStatisticWithSessionInfoModel> {
            val listOfTankStatisticWithSessionInfoModel =
                ArrayList<TankStatisticWithSessionInfoModel>()

            for (tankStatisticWithSessionInfo in listOfTankStatisticWithSessionInfo) {
                listOfTankStatisticWithSessionInfoModel.add(
                    TankStatisticWithSessionInfoModel(
                        tankStatisticWithSessionInfo.sessionInfo.date,
                        tankStatisticWithSessionInfo.sessionInfo.time,
                        TankAvgStatisticsPerSession(
                            tankStatisticWithSessionInfo.statistic.statistic.avgDamageDealtPerSession,
                            tankStatisticWithSessionInfo.statistic.statistic.battlesPerSession,
                            tankStatisticWithSessionInfo.statistic.statistic.percentageOfWinsPerSession,
                            tankStatisticWithSessionInfo.statistic.statistic.tier,
                            tankStatisticWithSessionInfo.statistic.statistic.name,
                            tankStatisticWithSessionInfo.statistic.statistic.nation,
                            tankStatisticWithSessionInfo.statistic.statistic.urlPreview,
                        )
                    )
                )
            }

            return listOfTankStatisticWithSessionInfoModel
        }

        fun SessionInfoWithTanksStatistics.toTanksStatisticsModelFinal() =
            TanksStatisticsModelFinal(
                sessionInfo.date,
                sessionInfo.time,
                sessionInfo.totalSessionStatistic,
                statistics.map {
                    TankAvgStatisticsPerSession(
                        it.statistic.avgDamageDealtPerSession,
                        it.statistic.battlesPerSession,
                        it.statistic.percentageOfWinsPerSession,
                        it.statistic.tier,
                        it.statistic.name,
                        it.statistic.nation,
                        it.statistic.urlPreview
                    )
                }
            )
    }
}