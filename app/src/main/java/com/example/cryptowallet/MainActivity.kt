package com.example.cryptowallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.cryptowallet.Activitys.FavCoinActivity
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.Fragments.HomeFragment
import com.example.cryptowallet.Fragments.ProfileFragment
import com.example.cryptowallet.Fragments.WalletFragment
import com.example.cryptowallet.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarView
import me.ibrahimsn.lib.OnItemReselectedListener
import me.ibrahimsn.lib.OnItemSelectedListener

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
                1 -> changeFragments(WalletFragment())
                else -> changeFragments(ProfileFragment())
            }
        }

    }

        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            menuInflater.inflate(R.menu.fav_coins_menu, menu)
            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            return when (item.itemId) {
                R.id.idFav_coin_menu -> {
                    val intent = Intent(this, FavCoinActivity::class.java)
                    startActivity(intent)
                    true
                }

                else -> super.onOptionsItemSelected(item)
            }
        }




    private fun changeFragments(frag : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer,frag)
            .addToBackStack(null)
            .commit()
    }
}