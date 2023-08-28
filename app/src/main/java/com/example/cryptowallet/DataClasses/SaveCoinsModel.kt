package com.example.cryptowallet.DataClasses
data class SaveCoinsModel(
    var coinId: Long,
    var coinName: String,
    var timeStamp: String
) {
    // Add a public no-argument constructor
    constructor() : this(0, "", "")
}
