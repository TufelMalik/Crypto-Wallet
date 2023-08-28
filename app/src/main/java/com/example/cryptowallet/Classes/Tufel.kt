package com.example.cryptowallet.Classes

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.DataClasses.SaveCoinsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Tufel {


    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference


    fun getCurrentDateAndTime(): Pair<String, String> {
        val currentDateTime = LocalDateTime.now()

        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val timeFormatter = DateTimeFormatter.ofPattern("h:mm a")

        val formattedDate = currentDateTime.format(dateFormatter)
        val formattedTime = currentDateTime.format(timeFormatter)

        return Pair(formattedDate, formattedTime)
    }

    fun getCurrentDate(): String {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("h:mm a dd/MM/yyyy")
        return currentDateTime.format(formatter)
    }

    fun isOnline(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false

        return activeNetwork.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun saveFavCoinstoDB(name : String,id : Long){
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)
        val time = SaveCoinsModel(id,name,getCurrentDate())
        db.child(name).setValue(time)
    }
    fun unSaveCointoDB(name : String){
        auth = FirebaseAuth.getInstance()
        db =  FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)
        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (id1 in snapshot.children) {
                    val data = id1.getValue(SaveCoinsModel::class.java)!!.coinName
                    if (data == name) {
                        id1.ref.removeValue()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun setRVLayoutOrientationManger(orientation: Int, context: Context, rv: RecyclerView) {
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            rv.layoutManager = GridLayoutManager(context, 3)
        }else if(orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.layoutManager = GridLayoutManager(context, 2)
        }
    }
}