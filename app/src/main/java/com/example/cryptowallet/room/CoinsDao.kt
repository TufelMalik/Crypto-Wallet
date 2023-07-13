package com.example.cryptowallet.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cryptowallet.DataClasses.CryptoCurrency


@Dao
interface CoinsDao {

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertCoins(cryptoCurrency: CryptoCurrency)
//
//    @Query("SELECT * FROM coins_table")
//    fun getAllCoins(): LiveData<List<CryptoCurrency>>
}
