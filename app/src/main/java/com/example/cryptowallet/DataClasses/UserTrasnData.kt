package com.example.cryptowallet.DataClasses

import java.util.Date

data class UserTrasnData(
    val userId : String?,
    val buyTra : String?,
    val sellTra : String?,
    val buyTimeStamp : String?,
    val sellTimeStamp : String?,
    val buyPrice : Double?,
    val sellPrice : Double?,
    val coinName : String?,

)
