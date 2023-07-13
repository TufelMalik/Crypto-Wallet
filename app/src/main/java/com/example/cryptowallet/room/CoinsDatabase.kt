package com.example.cryptowallet.room


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cryptowallet.DataClasses.CryptoCurrency


//@TypeConverters(value = [JsonConverter_::class])
//@Database(entities = [CryptoCurrency::class], version = 1)
abstract class CoinsDatabase : RoomDatabase() {
//    abstract fun coinsDao(): CoinsDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: CoinsDatabase? = null
//
//        fun getInstance(context: Context): CoinsDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    CoinsDatabase::class.java,
//                    "crypto_wallet_db"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
}
