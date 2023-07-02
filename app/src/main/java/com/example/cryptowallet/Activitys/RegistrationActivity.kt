package com.example.cryptowallet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cryptowallet.MainActivity
import com.example.cryptowallet.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth

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
                val auth = FirebaseAuth.getInstance()
                auth.createUserWithEmailAndPassword(
                    binding.etEmailReg.text.toString(),
                    binding.etPassReg.text.toString()
                )
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this@RegistrationActivity, "Data Saved", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                        } else {
                            Toast.makeText(this@RegistrationActivity, "Failed to save data", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(this@RegistrationActivity, "Failed to save data: ${exception.message}", Toast.LENGTH_SHORT).show()
                        Log.e("Firebase", "Failed to save data", exception)
                    }

            }
        }
    }
}