package com.example.cryptowallet.DataClasses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.cryptowallet.room.JsonConverter_
import java.io.Serializable
import java.util.Date

@Entity(tableName = "coins_table")
data class CryptoCurrency(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,
    val auditInfoList: List<AuditInfo> = emptyList(),
    val circulatingSupply: Double = 0.0,
    val cmcRank: Double = 0.0,
    var dateAdded: String? = null,
    val isActive: Double = 0.0,
    val isAudited: Boolean = false,
    val lastUpdated: String? = null,
    val marketPairCount: Double = 0.0,
    val maxSupply: Long = 0L,
    val name: String? = null,
    val platform: Platform? = null,
    val quotes: List<Quote> = emptyList(),
    val selfReportedCirculatingSupply: Double = 0.0,
    val slug: String? = null,
    val symbol: String? = null,
    val tags: List<String> = emptyList(),
    var isChecked: Boolean = false,
    val totalSupply: Double = 0.0,
    var dateAdded2: Date? = null
) : Serializable