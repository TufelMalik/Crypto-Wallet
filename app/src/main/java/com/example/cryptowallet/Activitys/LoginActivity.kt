package com.example.cryptowallet.Activitys

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.cryptowallet.Classes.Users
import com.example.cryptowallet.MainActivity
import com.example.cryptowallet.databinding.ActivityLoginBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class
LoginActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityLoginBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.hide()


        binding.btnGotoReg.setOnClickListener {
            startActivity(Intent(this,RegistrationActivity::class.java))
            val database = FirebaseDatabase.getInstance().reference
            val usersRef = database.child("users")
            val userId = usersRef.push().key

//            val user = User

            usersRef.child(userId!!).setValue("John Doe")
                .addOnCompleteListener {task->
                    if (task.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Data Saved", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "1.-Failed to save data", Toast.LENGTH_SHORT).show()
                    }

                }


        }

        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this,MainActivity::class.java))
        }


}


}
