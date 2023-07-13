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

    val id: Long = 0L,
    @ColumnInfo(name = "auditInfoList")
    val auditInfoList: List<AuditInfo> = emptyList(),
    @ColumnInfo(name = "circulatingSupply")
    val circulatingSupply: Double = 0.0,
    @ColumnInfo(name = "cmcRank")
    val cmcRank: Double = 0.0,
    @ColumnInfo(name = "dateAdded")
    var dateAdded: String? = null,
    @ColumnInfo(name = "isActive")
    val isActive: Double = 0.0,
    @ColumnInfo(name = "isAudited")
    val isAudited: Boolean = false,
    @ColumnInfo(name = "lastUpdated")
    val lastUpdated: String? = null,
    @ColumnInfo(name = "marketPairCount")
    val marketPairCount: Double = 0.0,
    @ColumnInfo(name = "maxSupply")
    val maxSupply: Long = 0L,
    @ColumnInfo(name = "name")
    val name: String? = null,
    @TypeConverters(JsonConverter_::class)
    @ColumnInfo(name = "platform")
    val platform: Platform? = null,
    @ColumnInfo(name = "quotes")
    val quotes: List<Quote> = emptyList(),
    @ColumnInfo(name = "selfReportedCirculatingSupply")
    val selfReportedCirculatingSupply: Double = 0.0,
    @ColumnInfo(name = "slug")
    val slug: String? = null,
    @ColumnInfo(name = "symbol")
    val symbol: String? = null,
    @ColumnInfo(name = "tags")
    val tags: List<String> = emptyList(),
    @ColumnInfo(name = "isChecked")
    var isChecked: Boolean = false,
    @ColumnInfo(name = "totalSupply")
    val totalSupply: Double = 0.0,
    @TypeConverters(JsonConverter_::class)
    @ColumnInfo(name = "date_added")
    var dateAdded2: Date? = null
) : Serializable {
    // ...
}