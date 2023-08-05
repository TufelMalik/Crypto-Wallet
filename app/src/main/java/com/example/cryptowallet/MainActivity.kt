package com.example.cryptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.Fragments.HomeFragment
import com.example.cryptowallet.Fragments.ProfileFragment
import com.example.cryptowallet.Fragments.SavedCoinsFragment
import com.example.cryptowallet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var searchView: SearchView
    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        if(Tufel.isOnline(this@MainActivity)){
            binding.noInternetAnimationHome.visibility = View.VISIBLE
        }else{
            binding.noInternetAnimationHome.visibility = View.GONE
        }

        // By Default Home Fragement
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, HomeFragment())
            .commit()


        changeFragments(HomeFragment())

        binding.bottomBar.setOnItemSelectedListener {pos ->
            when (pos) {
                0 -> changeFragments(HomeFragment())
                1 -> changeFragments(SavedCoinsFragment())
                else -> changeFragments(ProfileFragment())
            }
        }

    }






    private fun changeFragments(frag : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer,frag)
            .addToBackStack(null)
            .commit()
    }
}