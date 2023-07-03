package com.example.cryptowallet.Activitys

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.cryptowallet.DataClasses.Users
import com.example.cryptowallet.MainActivity
import com.example.cryptowallet.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityRegistrationBinding.inflate(layoutInflater)
    }


    val users = Users()
    val auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.circularProgressBarReg.visibility = View.GONE
        supportActionBar?.hide()
        binding.btngotoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnReg.setOnClickListener {
            if (binding.etNameReg.text.isEmpty()) {
                binding.etNameReg.error = "Enter Full Name"
            } else if (binding.etEmailReg.text.isEmpty()) {
                binding.etPassReg.error = "Email Address"
            } else if (binding.etNameReg.text.isEmpty()) {
                binding.etPassReg.error = "Password"
            } else {
                binding.circularProgressBarReg.visibility = View.VISIBLE
                registerTheUser()
            }
        }



    }
    private fun registerTheUser() {
        val email = binding.etEmailReg.text.toString()
        val pass = binding.etPassReg.text.toString()
        val name = binding.etNameReg.text.toString()
        auth.createUserWithEmailAndPassword(
           email,
            pass
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    binding.circularProgressBarReg.visibility = View.GONE
                    addUserDataOnDB(name,email,pass)
                    startActivity(Intent(this@RegistrationActivity, MainActivity::class.java))
                } else {
                    binding.circularProgressBarReg.visibility = View.GONE
                    Toast.makeText(this@RegistrationActivity, "Something Went Wrong !!!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this@RegistrationActivity, "Registration Failed.\nPleas try again later...: ${exception.message}", Toast.LENGTH_SHORT).show()
                Log.e("Firebase", "Failed to save data", exception)
                binding.circularProgressBarReg.visibility = View.GONE
            }

    }

    private fun addUserDataOnDB(name: String?, email: String, pass: String) {
        val database = FirebaseDatabase.getInstance().getReference("Users")

        val user = Users().apply {
            userAuth(auth.uid.toString(),name, email, pass)
        }

        database.child(auth.uid.toString()).setValue(user)
            .addOnSuccessListener {
                Toast.makeText(this@RegistrationActivity, "Registration Successfully done.", Toast.LENGTH_SHORT).show()
                binding.circularProgressBarReg.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(this@RegistrationActivity, "Registration Failed.\nPlease try again later...", Toast.LENGTH_SHORT).show()
                binding.circularProgressBarReg.visibility = View.GONE
            }
    }


}