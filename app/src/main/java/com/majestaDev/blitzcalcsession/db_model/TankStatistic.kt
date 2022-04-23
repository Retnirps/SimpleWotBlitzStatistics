package com.majestaDev.blitzcalcsession.db_model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.majestaDev.blitzcalcsession.model.TankAvgStatisticsPerSession

@Entity(
    tableName = "tank_statistic",
    foreignKeys = [ForeignKey(
        entity = SessionInfo::class,
        parentColumns = ["infoId"],
        childColumns = ["sessionOfId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class TankStatistic(
    @PrimaryKey(autoGenerate = true)
    val statisticsId: Long,
    val sessionOfId: Long,
    @Embedded
    val statistic: TankAvgStatisticsPerSession
)
