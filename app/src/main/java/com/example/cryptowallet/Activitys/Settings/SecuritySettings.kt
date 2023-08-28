package com.example.cryptowallet.Activitys.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptowallet.databinding.ActivitySecuritySettingsBinding

class SecuritySettings : AppCompatActivity() {
    private lateinit var binding : ActivitySecuritySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecuritySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar!!.hide()

        binding.securityBackButton.setOnClickListener {
            onBackPressed()
        }
    }
}