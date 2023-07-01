package com.example.cryptowallet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.cryptowallet.MainActivity
import com.example.cryptowallet.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityRegistrationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()
        binding.btngotoLogin.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
        }
        binding.btnReg.setOnClickListener {
            if(binding.etNameReg.text.toString().isEmpty()){
                binding.etNameReg.error ="Enter Full Name"
            }else  if(binding.etEmailReg.text.toString().isEmpty()){
                binding.etEmailReg.error ="Email Address"
            }else  if(binding.etPassReg.text.toString().isEmpty()){
                binding.etPassReg.error ="Password"
            }else {
                startActivity(Intent(this, MainActivity::class.java))
            }
        }
    }
}