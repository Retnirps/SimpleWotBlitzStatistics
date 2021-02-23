package com.majestaDev.blitzcalcsession.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tanks_statistics_table")
data class TanksStatisticsData(
    @PrimaryKey(autoGenerate = true) var id: Int,
    @ColumnInfo(name = "timestamp") var timestampOnStart: Long,
    @ColumnInfo(name = "blob_statistics", typeAffinity = ColumnInfo.BLOB) var statistic: ByteArray
)