package com.example.cryptowallet.DataClasses
data class SaveCoinsModel(
    var coinId: Long,
    var coinName: String,
    var time : String,
    var date : String
) {
     constructor() : this(0, "", "","")
}
