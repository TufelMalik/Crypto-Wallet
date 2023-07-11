package com.example.cryptowallet.Classes

import android.content.Context
import android.content.res.Configuration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.DataClasses.CryptoCurrency

object Tufel {

    fun setRVLayoutOrientationManger(orientation: Int, context: Context, rv: RecyclerView) {
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            rv.layoutManager = GridLayoutManager(context, 3)
        }else if(orientation == Configuration.ORIENTATION_PORTRAIT){
            rv.layoutManager = GridLayoutManager(context, 2)
        }
    }
}