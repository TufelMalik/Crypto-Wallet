import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.Classes.Constants
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.repository.CoinRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CoinViewModel(private val repository: CoinRepository) : ViewModel() {
//
//    fun getCoinData(): LiveData<List<CryptoCurrency>> {
//        return repository.getCoins()
//    }
//
//    fun insertCoins(cryptoCurrency: CryptoCurrency) {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.insertCoins(cryptoCurrency)
//        }
//    }
}
