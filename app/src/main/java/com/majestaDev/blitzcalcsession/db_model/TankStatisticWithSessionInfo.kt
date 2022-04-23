package com.majestaDev.blitzcalcsession.db_model

import androidx.room.Embedded
import androidx.room.Relation
import com.majestaDev.blitzcalcsession.model.IListItem

data class TankStatisticWithSessionInfo(
    @Embedded
    val statistic: TankStatistic,
    @Relation(parentColumn = "sessionOfId", entityColumn = "infoId")
    val sessionInfo: SessionInfo
) : IListItem
