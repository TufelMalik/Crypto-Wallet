package com.example.cryptowallet.Activitys

import CoinAdapter
import CoinViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.isNotEmpty
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.CoinViewModelFactory
import com.example.cryptowallet.Classes.Constants
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.ActivityDetailedBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding
    private lateinit var viewModel: CoinViewModel

    //    private val item : DetailedActivityArgs by navArgs()
    private var data: CryptoCurrency? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val actionBar = supportActionBar

        val data = intent.getStringExtra("data") as? CryptoCurrency
        if (data != null) {
            setUpDetails(data)
        }
        binding.backStackButton.setOnClickListener {
            onBackPressed()
        }
        actionBar!!.hide()
        setRecyclerView()
        getCoinDataFromAPI()


        binding.button.setOnClickListener {
            // 1Month

        }

        binding.button1.setOnClickListener {
            // 1Week

        }

        binding.button2.setOnClickListener {
            // 24 Hour

        }

        binding.button3.setOnClickListener {
            // 4 Hour

        }

        binding.button4.setOnClickListener {
            // 1 hour
        }

        binding.button5.setOnClickListener {
            // 15 Min

        }
    }

    private fun setRecyclerView() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData(Constants.LAST_URL)

            withContext(Dispatchers.Main) {

                if (result != null) {
                    binding.detailRecyclerView.adapter = CoinAdapter(this@DetailedActivity, result.body()!!.data.cryptoCurrencyList)
                    // Log.d("Tufel", result.body()!!.data.cryptoCurrencyList.toString())
                   binding.detailRecyclerView.layoutManager= GridLayoutManager(this@DetailedActivity,2)

                }
            }

        }
    }


    private fun setUpDetails(data: CryptoCurrency?) {
        binding.detailCoinShortName.text = data!!.name
        binding.circularProgressBar.visibility = View.VISIBLE
        binding.detailPriceTextView.text = data!!.id.toString()
    }


    private fun getCoinDataFromAPI() {
        val apiInterface = ApiUtilities.getInstace().create(ApiInterface::class.java)
        val intent = intent
        val coinId = intent.getIntExtra("data", 1027)
        Constants.coinID = coinId
        Toast.makeText(this@DetailedActivity, coinId.toString(), Toast.LENGTH_SHORT).show()
        val factory = CoinViewModelFactory(apiInterface)
        viewModel = ViewModelProvider(this, factory).get(CoinViewModel::class.java)

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result = apiInterface.getMarketData(Constants.LAST_URL_ID)
                withContext(Dispatchers.Main) {
                    if (result.isSuccessful) {
                        val cryptoCurrencyList = result.body()?.data?.cryptoCurrencyList
                        if (!cryptoCurrencyList.isNullOrEmpty()) {
                            val coinData = cryptoCurrencyList.find { it.id == coinId }
                            coinData?.let { coin ->
                                binding.circularProgressBar.visibility = View.GONE
                                // Update your views with the coin data
                                binding.detailCoinShortName.text = coin.symbol
                                binding.detailPriceTextView.text = coin.quotes[0].price.toString()
                                binding.detaillChartWebView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${coin.symbol}")
                                Glide.with(this@DetailedActivity)
                                    .load("https://s2.coinmarketcap.com/static/img/coins/64x64/${coin.id}.png")
                                    .thumbnail(Glide.with(this@DetailedActivity).load(R.drawable.loading1))
                                    .into(binding.detailImageView)
                                binding.detailPriceTextView.text =coin.name
                                if (coin.quotes[0].percentChange1h > 0) {
                                    binding.detailChangeTextView.setTextColor(this@DetailedActivity.resources.getColor(R.color.green))
                                    binding.detailChangeTextView.text = " ${String.format("%.2f", coin.quotes[0].percentChange1h)} %"
                                    binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
                                } else {
                                    binding.detailChangeTextView.setTextColor(this@DetailedActivity.resources.getColor(R.color.red))
                                    binding.detailChangeTextView.text = " ${String.format("%.2f", coin.quotes[0].percentChange1h)} %"
                                    binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
                                }
                                binding.detailPriceTextView.text =   "$ ${String.format("%.2f", coin.quotes[0].price)} %"


                            }
                        }
                    } else {
                        Toast.makeText(this@DetailedActivity, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailedActivity, "Something went wrong.\n${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }




}