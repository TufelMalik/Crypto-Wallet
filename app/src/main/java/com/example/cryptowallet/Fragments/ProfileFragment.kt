package com.example.cryptowallet.Fragments

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.cryptowallet.DataClasses.Users
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.auth.User
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {
    private lateinit var binding : FragmentProfileBinding
    val auth = FirebaseAuth.getInstance()
    val userId = auth.uid.toString()
    private lateinit var imgUri: Uri
    val storage = FirebaseStorage.getInstance()
    val database = FirebaseDatabase.getInstance().getReference("Users")

    private val PICK_IMAGE_REQUEST = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?



    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)



        binding.userImgProfile.setOnClickListener {
            openGallery()
            uplodeImageOnDatabase()
        }


        return binding.root
    }
    private fun uplodeImageOnDatabase() {
        val refrence = storage.reference.child("UserImages").child(userId)
        refrence.putFile(imgUri)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    task.result?.storage?.downloadUrl?.addOnSuccessListener { downloadUri ->

                        database.child(userId).child("uimg").setValue(downloadUri.toString())
                        Toast.makeText(context, "Profile picture updated.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Failed to update profile picture!", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update profile picture!", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()) {
                    val user = snapshot.getValue(Users::class.java)
                    binding.userNameProfile.text = user!!.name
                    if(user.uimg != null){
                        context?.let { Glide.with(it).load(user.uimg).into(binding.userImgProfile) }
                    }else
                    {
                        binding.userImgProfile.setImageResource(R.drawable.person)
                    }

                }


                val user = snapshot.getValue(Users::class.java)


            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Something went wrong !!!", Toast.LENGTH_SHORT).show()
            }
        })

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            if (data.data != null) {
                imgUri = data.data!!
                binding.userImgProfile.setImageURI(imgUri)
            }
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }


}