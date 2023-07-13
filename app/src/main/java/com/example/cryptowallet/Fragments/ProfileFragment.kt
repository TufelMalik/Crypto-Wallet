package com.example.cryptowallet.Fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.cryptowallet.Activitys.Settings.AccountSettings
import com.example.cryptowallet.Activitys.Settings.CurrencySettings
import com.example.cryptowallet.Activitys.Settings.NotificationSettings
import com.example.cryptowallet.Activitys.Settings.OtherSettings
import com.example.cryptowallet.Activitys.Settings.SecuritySettings
import com.example.cryptowallet.DataClasses.Users
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class ProfileFragment : Fragment() {
    private lateinit var binding: FragmentProfileBinding
    private lateinit var imgUri: Uri
    private val auth = FirebaseAuth.getInstance()
    private val userId = auth.uid.toString()
    private val storage = FirebaseStorage.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("Users")

    private val launcher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        // Handle the selected image URI here
        if (uri != null) {
            imgUri = uri
            binding.userImgProfile.setImageURI(imgUri!!)
            uplodeImageOnDatabase()
        }else{

        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)


        setData()
        binding.idAccSetting.setOnClickListener {
            openSettingsActivity(AccountSettings::class.java)
        }
        binding.idCurrecncySetting.setOnClickListener {
            openSettingsActivity(CurrencySettings::class.java)
        }
        binding.idNotificationSetting.setOnClickListener {
            openSettingsActivity(NotificationSettings::class.java)
        }
        binding.idSecuritySetting.setOnClickListener {
            openSettingsActivity(SecuritySettings::class.java)
        }
        binding.txtVerison.setOnClickListener {
            openSettingsActivity(OtherSettings::class.java)
        }



        val function: (View) -> Unit = {
            openGallery()
        }



        binding.userImgProfile.setOnClickListener(function)

        return binding.root
    }

    private fun openSettingsActivity(activityClass: Class<*>) {
        val intent = Intent(context, activityClass)
        startActivity(intent)
    }

    private fun uplodeImageOnDatabase() {
        val reference = storage.reference.child("UserImages").child(userId)
        reference.putFile(imgUri!!)
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

    override fun onResume() {
        super.onResume()
        setData()
    }

    private fun setData() {
        database.child(userId).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val user = snapshot.getValue(Users::class.java)
                binding.userNameProfile.text = user?.name
                binding.txtCurrentBalProfile.text = user!!.bal.toString()
                if (user?.uimg != null) {
                   Glide.with(requireContext()).load(user.uimg).thumbnail(Glide.with(requireContext()).load(R.drawable.person)).into(binding.userImgProfile)
                } else {
                    binding.userImgProfile.setImageResource(R.drawable.person)
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        launcher.launch("image/*")
    }
}
