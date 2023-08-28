package com.example.cryptowallet.Activitys



import SavedCoinAdapter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.SharedPrefsHelper
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.Fragments.CoinAdapter
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
    private lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var savedCoinAdapter: SavedCoinAdapter
    private lateinit var coinList : ArrayList<CryptoCurrency>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavCoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId= auth.currentUser!!.uid.toString()
        sharedPrefsHelper = SharedPrefsHelper(this)
        getDataFromDB()



    }private fun getDataFromDB() {
        database.getReference("FavCoin").child(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val res = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData()
                    coinList = ArrayList()
                    withContext(Dispatchers.Main) {
                        val apiDataList = res.body()?.data?.cryptoCurrencyList

                        if (apiDataList != null) {
                            for (snapshot1 in snapshot.children) {
                                val savedCoinsId = snapshot1.getValue(Long::class.java)
                                val matchingCoin = apiDataList.find { it.id == savedCoinsId }
                                matchingCoin?.let {
                                    coinList.add(it)
                                }
                            }
                            binding.favCoinRV.adapter = CoinAdapter(this@FavCoinActivity,coinList)
                            binding.favCoinRV.layoutManager = GridLayoutManager(this@FavCoinActivity,2)
                        }

                        // Now, you can use the coinList for further processing
                        // For example, pass it to the RecyclerView adapter and set the layout manager
                        Tufel.setRVLayoutOrientationManger(resources.configuration.orientation, this@FavCoinActivity, binding.favCoinRV)
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