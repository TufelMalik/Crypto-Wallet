package com.example.cryptowallet.Classes

import CoinViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptowallet.repository.CoinRepository

class CoinViewModelFactory(private val repository: CoinRepository) : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(CoinViewModel::class.java)) {
//            return CoinViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
}
