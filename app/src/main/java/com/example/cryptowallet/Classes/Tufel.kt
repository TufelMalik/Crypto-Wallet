package com.example.cryptowallet.Classes

import android.content.Context
import android.content.res.Configuration
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.CheckBox
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.DataClasses.SaveCoinsModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.Random

object Tufel {


    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference
    const val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L // 24 hours * 60 minutes * 60 seconds * 1000 milliseconds


    fun parseTimestamp(timestampString: String): Long {
        val dateFormat = SimpleDateFormat("hh:mm a dd/MM/yyyy", Locale.getDefault())
        try {
            val date = dateFormat.parse(timestampString)
            return date?.time ?: 0L
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return 0L
    }

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
    fun saveFavCoinstoDB(checkBox: CheckBox, name: String, id: Long) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)
        val currentTime = getCurrentDate()

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val savedCoins = ArrayList<Long>()
                val savedTimeMap = HashMap<Long, String>()
                for (snap in snapshot.children) {
                    val savedCoinsId = snap.child("coinId").getValue(Long::class.java)
                    val savedTime = snap.child("timeStamp").getValue(String::class.java) ?: ""
                    savedCoinsId?.let { savedCoins.add(it) }
                    savedTimeMap[savedCoinsId!!] = savedTime
                }
                if (savedCoins.contains(id)) {
                    val savedTime = savedTimeMap[id] ?: ""

                    if (savedTime != currentTime) {
                        val coinValue = SaveCoinsModel(id, name, savedTime)
                        db.child(name).setValue(coinValue)
                        Log.d("CoinAdapter", "False : $coinValue")
                    } else {
                        val time = SaveCoinsModel(id, name, currentTime)
                        db.child(name).setValue(time)
                        Log.d("CoinAdapter", "True : $time")
                    }
                } else {
                    val time = SaveCoinsModel(id, name, currentTime)
                    db.child(name).setValue(time)
                    Log.d("CoinAdapter", "New Entry: $time")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("CoinAdapter", "Database error: $error")
            }
        })
    }


//
//    fun saveFavCoinstoDB(checkBox: CheckBox, name: String, id: Long) {
//        auth = FirebaseAuth.getInstance()
//        db = FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)
//        val currentTime = getCurrentDate()
//
//        db.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val savedCoins = ArrayList<Long>()
//                val savedTimeStamp = ArrayList<String>()
//                for (snap in snapshot.children) {
//                    val savedCoinsId = snap.child("coinId").getValue(Long::class.java)
//                    val savedTime = snap.child("timeStamp").getValue(String::class.java) ?: ""
//                    savedCoinsId?.let { savedCoins.add(it) }
//                    savedTime.let { savedTimeStamp.add(it) }
//                }
//                Log.d("CoinAdapter", "Saved Coins: $savedCoins")
//                if(savedCoins.isEmpty()){
//                    val time = SaveCoinsModel(id, name, currentTime)
//                    db.child(name).setValue(time)
//                }
//                if (savedCoins.contains(id)) {
//                    if (!savedTimeStamp.equals(currentTime)) {
//                        val coinValue = SaveCoinsModel(id,name, savedTimeStamp[0])
//                        db.child(name).setValue(coinValue)
//                        Log.d("CoinAdapter", "False : $coinValue")
//                    } else {
//                        val time = SaveCoinsModel(id, name, currentTime)
//                        db.child(name).setValue(time) // Update the timestamp
//                        Log.d("CoinAdapter", "True : $time")
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                TODO("Not yet implemented")
//            }
//        })
//    }



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