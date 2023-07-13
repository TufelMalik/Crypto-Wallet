package com.example.cryptowallet.Activitys.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.ActivityCurrencySettingsBinding

class CurrencySettings : AppCompatActivity() {
    private lateinit var binding : ActivityCurrencySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrencySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.currencyBackButton.setOnClickListener {
            onBackPressed()
        }
    }

}