package com.example.cryptowallet.Activitys

import android.R
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptowallet.MainActivity
import com.example.cryptowallet.databinding.ActivityLoginBinding
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.DoubleBounce
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase


class
LoginActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }

    val database = FirebaseDatabase.getInstance().getReference("Users")
    val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()

        binding.progressBarLogin.visibility = View.GONE
        binding.btnGotoReg.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))

        }


        binding.btnLogin.setOnClickListener {
            if (binding.etEmailLogin.text.isEmpty()) {
                binding.etEmailLogin.error = "Email Address"
            } else if (binding.etPassLogin.text.isEmpty()) {
                binding.etPassLogin.error = "Enter Password"
            }else {
                binding.progressBarLogin.visibility = View.VISIBLE
                loginTheUser()
            }
        }

        if(auth.currentUser != null){
            startActivity(Intent(this,MainActivity::class.java))
        }

}

    private fun loginTheUser() {
        val email = binding.etEmailLogin.text.toString()
        val pass = binding.etPassLogin.text.toString()


        auth.signInWithEmailAndPassword(email,pass)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    binding.progressBarLogin.visibility = View.GONE
                    startActivity(Intent(this,MainActivity::class.java))
                    Toast.makeText(this@LoginActivity, "Login Successfully...", Toast.LENGTH_SHORT).show()
                }else{
                    binding.progressBarLogin.visibility = View.GONE

                    Toast.makeText(this@LoginActivity, "You Don't have account\nPleas SignUp first !", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                binding.progressBarLogin.visibility = View.GONE
                Toast.makeText(this@LoginActivity, "Login Failed !!!", Toast.LENGTH_SHORT).show()
            }
    }




}
