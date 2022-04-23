package com.majestaDev.blitzcalcsession.db_model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.majestaDev.blitzcalcsession.model.TotalTanksStatistics

@Entity(tableName = "session_info")
data class SessionInfo(
    @PrimaryKey(autoGenerate = true)
    val infoId: Long,
    val date: String,
    val time: String,
    @Embedded
    val totalSessionStatistic: TotalTanksStatistics
)
