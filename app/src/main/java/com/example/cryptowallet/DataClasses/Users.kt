package com.example.cryptowallet.DataClasses

data class Users(
    var userID : String?,
    var name : String?,
    var email : String?,
    var pass : String?,
    var bal : Double?,
    var uimg : String?,
    var userTras : List<UserTrasnData>
){

    fun userAuth(userID: String?,name: String?,email: String?, pass: String?){
        this.userID = userID
        this.name = name
        this.email = email
        this.pass = pass
    }

    constructor(): this("","","","",0.0,"", emptyList())
}