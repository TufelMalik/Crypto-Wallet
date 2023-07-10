package com.example.cryptowallet.Classes

// https://api.coinmarketcap.com/data-api/v3/cryptocurrency/listing?start=1&limit=500
//  https://s2.coinmarketcap.com/static/img/coins/64x64/ "+imgId+".png


object Constants {


    var coinID: Int = 1
    val BASE_URL = "https://api.coinmarketcap.com/"
    const val LAST_URL = "data-api/v3/cryptocurrency/listing?start=1&limit=500"
    var LAST_URL_ID= "data-api/v3/cryptocurrency/detail?id=${coinID}"
//    val WEB_CHART_URL = "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=\" + ${coin.symbol.toString()}" +
//            "USD&interval=D&hideside toolbar=1&hidetoptoolbar=1&symboledit=1&save image=1&toolbarbg=" +
//            "F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc/UTC&studies_overrides =" +
//            " {}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap" +
//            ".com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"




}