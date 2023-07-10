package com.example.cryptowallet.Activitys

import CoinAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.databinding.ActivityFavCoinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavCoinActivity : AppCompatActivity() {
    private lateinit var binding : ActivityFavCoinBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var userId : String
    private lateinit var coinList : ArrayList<CryptoCurrency>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId= auth.currentUser!!.uid.toString()

        getDataFromDB()



    }

    private fun getDataFromDB() {
        database.getReference("FavCoin").child(userId).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot1 in snapshot.children){
                    val coins = snapshot1.getValue(CryptoCurrency::class.java)

                    lifecycleScope.launch(Dispatchers.IO){
                        val res = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData()
                        if(res != null){
                           coinList = ArrayList()
                            withContext(Dispatchers.Main){
                                database.getReference("FavCoin").child(userId).addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        coinList.clear()
                                        for(i in snapshot.children){
                                           val coins = i.getValue(CryptoCurrency::class.java)
                                            coinList.add(coins!!)
                                            binding.favCoinRV.adapter = CoinAdapter(
                                                this@FavCoinActivity,
                                                coinList
                                            )
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        TODO("Not yet implemented")
                                    }

                                })
                                //   Setting Recycler View Orientation Same as Device Orientation
                                Tufel.setRVLayoutOrientationManger(resources.configuration.orientation,this@FavCoinActivity,binding.favCoinRV)
                            }
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }



    override fun onBackPressed() {
        super.onBackPressed()

    }
}