package com.example.cryptowallet.Activitys


import CoinViewModel
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.cryptowallet.Classes.Constants
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.ActivityDetailedBinding

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

        data = intent.getStringExtra("data") as? CryptoCurrency
        if (data != null) {
            setUpDetails(data)
        }
        binding.backStackButton.setOnClickListener {
            onBackPressed()
        }
        actionBar!!.hide()
        getCoinDataFromAPI()

        saveCoinOnWatchList()

    }

    private fun saveCoinOnWatchList() {
        val cb = binding.cbWatchlistDet
//        cb.setOnCheckedChangeListener { compoundButton, b ->
//            if(cb.isChecked){
//                binding.cbWatchlistDet.setBackgroundColor(resources.getColor(R.color.black))
//            }else{
//                binding.cbWatchlistDet.setBackgroundColor(resources.getColor(R.color.gray))
//            }
//        }
    }


    private fun setUpDetails(data: CryptoCurrency?) {
        binding.detailCoinShortName.text = data!!.name
        binding.circularProgressBar.visibility = View.VISIBLE
        binding.detailPriceTextView.text = data!!.id.toString()
        binding.txt24DaysPrice.text = data!!.quotes[0].percentChange24h.toString()
        binding.txt7DaysPrice.text = data!!.quotes[0].percentChange7d.toString()
        binding.txt30DaysPrice.text = data!!.quotes[0].percentChange30d.toString()
        binding.txt90DaysPrice.text = data!!.quotes[0].percentChange90d.toString()
    }


    private fun getCoinDataFromAPI() {

        val intent = intent
        val coinId = intent.getIntExtra("data", 1027)
        Constants.coinID = coinId

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
        Toast.makeText(this@DetailedActivity,coin.symbol,Toast.LENGTH_SHORT).show()
        binding.detaillChartWebView.loadUrl(" https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${coin.symbol}USD&interval=${s.toString()}&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT")


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
        binding.detaillChartWebView.loadUrl(" https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${coin.symbol}USD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT")


    }


}

//"https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=ETCUSD&interval=D&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"

