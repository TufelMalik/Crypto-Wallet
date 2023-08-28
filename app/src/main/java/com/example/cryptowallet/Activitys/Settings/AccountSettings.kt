package com.example.cryptowallet.Activitys.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptowallet.databinding.ActivityAccountSettingsBinding

class AccountSettings : AppCompatActivity() {
    private lateinit var binding : ActivityAccountSettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        binding.accountBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}