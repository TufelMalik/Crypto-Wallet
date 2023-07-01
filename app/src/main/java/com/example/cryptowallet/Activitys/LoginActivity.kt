package com.example.cryptowallet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptowallet.MainActivity
import com.example.cryptowallet.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btnGotoReg.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }
    }
}