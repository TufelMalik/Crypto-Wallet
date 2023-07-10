import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.Classes.Constants
import com.example.cryptowallet.DataClasses.CryptoCurrency
import kotlinx.coroutines.launch

class CoinViewModel(private val apiInterface: ApiInterface) : ViewModel() {

    fun getCoinData(coinId: Int): LiveData<CryptoCurrency> {
        val coinDataLiveData = MutableLiveData<CryptoCurrency>()

        viewModelScope.launch {
            try {
                val response = apiInterface.getMarketData()
                if (response.isSuccessful) {
                    val marketModel = response.body()
                    marketModel?.data?.cryptoCurrencyList?.let { coinList ->
                        if (coinList.isNotEmpty()) {
                            coinDataLiveData.postValue(coinList[0])
                        }
                    }
                } else {
                    // Handle API error
                }
            } catch (e: Exception) {
                // Handle network or API call exception
            }
        }

        return coinDataLiveData
    }
}
