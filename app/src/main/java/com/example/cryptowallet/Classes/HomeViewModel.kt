package com.example.cryptowallet.Classes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.DataClasses.CryptoCurrency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {
//    private val _currencyList = MutableLiveData<List<CryptoCurrency>>()
//    val currencyList: LiveData<List<CryptoCurrency>> get() = _currencyList
//
//    fun fetchCurrencyList() {
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData()
//            withContext(Dispatchers.Main) {
//                if (result != null && result.isSuccessful) {
//                    _currencyList.value = result.body()?.data?.cryptoCurrencyList
//                }
//            }
//        }
//    }
}
