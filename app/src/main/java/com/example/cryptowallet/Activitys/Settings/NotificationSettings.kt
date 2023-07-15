package com.example.cryptowallet.Activitys.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptowallet.R

class NotificationSettings : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_settings)
        supportActionBar!!.hide()
    }
}