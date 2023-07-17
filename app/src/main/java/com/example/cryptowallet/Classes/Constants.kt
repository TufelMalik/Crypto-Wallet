package com.example.cryptowallet.Classes

// https://api.coinmarketcap.com/data-api/v3/cryptocurrency/listing?start=1&limit=500
//  https://s2.coinmarketcap.com/static/img/coins/64x64/ "+imgId+".png


object Constants {


    var coinID: Int = 1
    val BASE_URL = "https://api.coinmarketcap.com/"
    const val LAST_URL = "data-api/v3/cryptocurrency/listing?start=1&limit=500"
    var LAST_URL_ID= "data-api/v3/cryptocurrency/detail?id=${coinID}"




}