package com.example.cryptowallet.Activitys

import CoinViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.CoinViewModelFactory
import com.example.cryptowallet.Classes.Constants
import com.example.cryptowallet.databinding.ActivityDetailedBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailedBinding
    private lateinit var viewModel: CoinViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar
        binding.backStackButton.setOnClickListener {
            onBackPressed()
        }
        actionBar!!.hide()


//        Constants.coinID = coinId
//        Toast.makeText(this@DetailedActivity,coinId.toString(),Toast.LENGTH_SHORT).show()
        getCoinDataFromAPI()


    }


    private fun getCoinDataFromAPI() {
        val apiInterface = ApiUtilities.getInstace().create(ApiInterface::class.java)
        val intent = intent
        val coinId = intent.getIntExtra("coinID",1027)
        Constants.coinID = coinId
        Toast.makeText(this@DetailedActivity,coinId.toString(),Toast.LENGTH_SHORT).show()
        val factory = CoinViewModelFactory(apiInterface)
        viewModel = ViewModelProvider(this, factory).get(CoinViewModel::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData(Constants.LAST_URL_ID)
                withContext(Dispatchers.Main) {
                    if (result.isSuccessful) {
                        val cryptoCurrencyList = result.body()?.data?.cryptoCurrencyList
                        if (!cryptoCurrencyList.isNullOrEmpty()) {
                            viewModel.getCoinData(coinId).observe(this@DetailedActivity, { coinData ->
                                // Update your views with the coin data
                                binding.detailCoinShortName.text = coinData.name
                                binding.detailPriceTextView.text = coinData.id.toString()
                                binding.detaillChartWebView.loadUrl("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/"+coinId+".png")
//                                Glide.with(this@DetailedActivity).load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/"+coinId+".png")
//                                    .into(binding.detaillChartWebView)
                            })
                            val coin = cryptoCurrencyList[0].quotes[0]
                            binding.detailCoinShortName.text = coin.name
                            binding.detailPriceTextView.text = coin.price.toString()

                        }
                    } else {
                        Toast.makeText(this@DetailedActivity,"Something went wrong !!!",Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailedActivity,"Something went wrong.\n"+e.message,Toast.LENGTH_SHORT).show()

            }
        }
    }



}