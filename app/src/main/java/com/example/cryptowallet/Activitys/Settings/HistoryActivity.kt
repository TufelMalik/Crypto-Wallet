package com.example.cryptowallet.Activitys.Settings

import SavedCoinAdapter
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.adapters.SearchViewBindingAdapter
import androidx.lifecycle.lifecycleScope
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.Constants
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.Classes.Tufel.getCurrentDate
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.DataClasses.SaveCoinsModel
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.ActivityHistoryActivityBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryActivity : AppCompatActivity() {
    private lateinit var binding : ActivityHistoryActivityBinding
    private lateinit var adapter : SavedCoinAdapter
    private lateinit var dataList : List<CryptoCurrency>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        binding.historyBackButton.setOnClickListener {
            onBackPressed()
        }

        getDataFromApi()
    }


    private fun getDataFromApi() {

        val time = SaveCoinsModel(dataList[0].id,dataList[0].name!!,getCurrentDate())
        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstace().create(ApiInterface::class.java)
            withContext(Dispatchers.Main) {
                dataList = result.getMarketData().body()!!.data.cryptoCurrencyList
                if (dataList != null) {
                    adapter = SavedCoinAdapter(this@HistoryActivity, dataList,time)
                    binding.historyRecyclerView.adapter = adapter
                    Log.d("Tufel", dataList.toString())
                    Tufel.setRVLayoutOrientationManger(
                        resources.configuration.orientation,
                        this@HistoryActivity,
                        binding.historyRecyclerView
                    )
                }
            }
        }
    }

}