package com.example.cryptowallet.Activitys


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.ActivityDetailedBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailedActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailedBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var db : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailedBinding.inflate(layoutInflater)
        setContentView(binding.root)

       supportActionBar!!.hide()


        val coinId = intent.getLongExtra("data",1027)
        if (coinId != null) {
            setUpDetails(coinId)
        }
        binding.backStackButton.setOnClickListener {
            onBackPressed()
        }

    }

    private fun saveDataOnDB(name: String?, id: Long) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)

        db.child(name!!)
            .setValue(id)
            .addOnCompleteListener {
                Toast.makeText(this@DetailedActivity, "$name Coin Saved", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setUpDetails(coinId : Long?) {
        if(coinId == null){
            return
        }
        lifecycleScope.launch(Dispatchers.IO){
            withContext(Dispatchers.IO) {
                val result = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData()
                val data1 = result.body()!!.data.cryptoCurrencyList
                if(data1 != null){
                    val data = data1.find {c -> c.id == coinId }
                    withContext(Dispatchers.Main){
                        if(data != null){
                            binding.circularProgressBar.visibility = View.GONE
                            setButtonsOnClick(data)
                            loadChart(data)
                            if(binding.cbWatchlistDet.callOnClick()){
                                saveDataOnDB(data.name,data.id)
                            }
                            binding.detailCoinFullName.text = data.name
                            binding.detailCoinShortName.text = data.symbol
                            Glide.with(this@DetailedActivity)
                                .load("https://s2.coinmarketcap.com/static/img/coins/64x64/${data.id}.png")
                                .thumbnail(Glide.with(this@DetailedActivity).load(R.drawable.loading13))
                                .into(binding.detailImageView)
                            binding.detailChangeTextView.text = String.format("%.2f %%", data.quotes[0].percentChange1h)
                            binding.detailPriceTextView.text = if (data.quotes[0].price > 0) "$ "+ String.format("%.4f %%", data.quotes[0].price)  else "$ "+String.format("%.4f %%", data.quotes[0].price)
                            checkPriceUpOrDown(data)
                            binding.txt24DaysPrice.text = String.format("%.4f %%", data.quotes[0].percentChange24h)
                            binding.txt7DaysPrice.text = String.format("%.4f %%", data.quotes[0].percentChange7d)
                            binding.txt30DaysPrice.text = String.format("%.4f %%", data.quotes[0].percentChange30d)
                            binding.txt90DaysPrice.text = String.format("%.4f %%", data.quotes[0].percentChange90d)
                        }
                    }

                }
            }
        }
    }

    private fun checkPriceUpOrDown(data: CryptoCurrency) {
        if(data.quotes[0].percentChange1h > 0){
            binding.detailChangeTextView.setTextColor(resources.getColor(R.color.green))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
        }else{
            binding.detailChangeTextView.setTextColor(resources.getColor(R.color.red))
            binding.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
        }
    }


    private fun setButtonsOnClick(coin: CryptoCurrency) {
        val oneMonth = binding.button
        val oneWeek = binding.button1
        val oneDay = binding.button2
        val fourHour = binding.button3
        val oneHour = binding.button4
        val fifteenMinute = binding.button5

        val clikcListner = View.OnClickListener {
            when(it.id){
                fifteenMinute.id -> loadChartData(it,"15",coin,oneDay,oneMonth,oneWeek,fourHour,oneHour)
                oneHour.id -> loadChartData(it,"1H",coin,oneDay,oneMonth,oneWeek,fourHour,fifteenMinute)
                fourHour.id -> loadChartData(it,"4H",coin,oneDay,oneMonth,oneWeek,fourHour,oneHour)
                oneDay.id -> loadChartData(it,"D",coin,fifteenMinute,oneMonth,oneWeek,fourHour,oneHour)
                oneWeek.id -> loadChartData(it,"W",coin,oneDay,oneMonth,fifteenMinute,fourHour,oneHour)
                oneMonth.id -> loadChartData(it,"M",coin,oneDay,fifteenMinute,oneWeek,fourHour,oneHour)
            }
        }
        fifteenMinute.setOnClickListener(clikcListner)
        oneHour.setOnClickListener(clikcListner)
        fourHour.setOnClickListener(clikcListner)
        oneDay.setOnClickListener(clikcListner)
        oneWeek.setOnClickListener(clikcListner)
        oneMonth.setOnClickListener(clikcListner)
    }

    private fun loadChartData(
        it: View?,
        s: String,
        coin: CryptoCurrency,
        oneDay: AppCompatButton,
        oneMonth: AppCompatButton,
        oneWeek: AppCompatButton,
        fourHour: AppCompatButton,
        oneHour: AppCompatButton
    ) {
        disableButtons(oneDay,oneMonth,oneWeek,fourHour,oneHour)
        it!!.setBackgroundResource(R.drawable.active_button)
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl("https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${coin.symbol}USD&interval=${s.toString()}&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT")


    }

    private fun disableButtons(oneDay: AppCompatButton, oneMonth: AppCompatButton, oneWeek: AppCompatButton, fourHour: AppCompatButton, oneHour: AppCompatButton) {
        oneDay.background = null
        oneMonth.background = null
        oneWeek.background = null
        fourHour.background = null
        oneHour.background = null
    }

    private fun loadChart(coin: CryptoCurrency) {
        binding.detaillChartWebView.settings.javaScriptEnabled = true
        binding.detaillChartWebView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        binding.detaillChartWebView.loadUrl(getChartUrl(coin.symbol))

    }

    private fun getChartUrl(symbol: String?): String {
         return "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"
    }


}

//"https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=ETCUSD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"

