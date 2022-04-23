package com.majestaDev.blitzcalcsession.db_model

import androidx.room.Embedded
import androidx.room.Relation
import com.majestaDev.blitzcalcsession.model.IListItem

data class SessionInfoWithTanksStatistics(
    @Embedded
    val sessionInfo: SessionInfo,
    @Relation(
        parentColumn = "infoId",
        entityColumn = "sessionOfId"
    )
    val statistics: List<TankStatistic>
) : IListItem
