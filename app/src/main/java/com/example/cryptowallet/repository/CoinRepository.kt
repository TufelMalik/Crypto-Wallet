package com.example.cryptowallet.repository

import androidx.lifecycle.LiveData
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.room.CoinsDao

class CoinRepository(private val coinsDao: CoinsDao) {

//    fun getCoins(): LiveData<List<CryptoCurrency>> {
//        return coinsDao.getAllCoins()
//    }
//
//    suspend fun insertCoins(cryptoCurrency: CryptoCurrency) {
//        coinsDao.insertCoins(cryptoCurrency)
//    }
}
