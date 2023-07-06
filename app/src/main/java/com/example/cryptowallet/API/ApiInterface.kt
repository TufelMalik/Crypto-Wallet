package com.example.cryptowallet.API

import retrofit2.http.GET
import com.example.cryptowallet.Classes.Constants.LAST_URL
import com.example.cryptowallet.DataClasses.MarketModel
import retrofit2.Response

interface ApiInterface {
    @GET(LAST_URL)
    suspend fun getMarketData(): Response<MarketModel>

}