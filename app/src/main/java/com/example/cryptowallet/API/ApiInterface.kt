package com.example.cryptowallet.API

import com.example.cryptowallet.Classes.Constants
import retrofit2.http.GET
import com.example.cryptowallet.DataClasses.MarketModel
import retrofit2.Response
import retrofit2.http.Query


interface ApiInterface {
    @GET(Constants.LAST_URL)
    suspend fun getMarketData(): Response<MarketModel>
}


