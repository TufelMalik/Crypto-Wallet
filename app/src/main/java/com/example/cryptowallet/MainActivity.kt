package com.example.cryptowallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.Fragments.HomeFragment
import com.example.cryptowallet.Fragments.ProfileFragment
import com.example.cryptowallet.Fragments.SavedCoinsFragment
import com.example.cryptowallet.databinding.ActivityMainBinding
import com.shashank.sony.fancytoastlib.FancyToast

class MainActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar!!.hide()
        if(Tufel.isOnline(this@MainActivity)){
            FancyToast.makeText(
                this@MainActivity, "Online", FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS, false
            ).show()
            binding.noInternetAnimationHome.visibility = View.VISIBLE
        }else{
            binding.noInternetAnimationHome.visibility = View.GONE
        }




        binding.txtHeading.text = getText(R.string.app_name)
        binding.imgLogoHeading.visibility = View.VISIBLE
        binding.txtHeading.setTextColor(ContextCompat.getColor(this, R.color.gold))


        // By Default Home Fragement
        changeFragments(HomeFragment())

        binding.bottomBar.setOnItemSelectedListener {pos ->
            when (pos) {
                0 -> {
                    changeFragments(HomeFragment())
                    binding.txtHeading.text = getText(R.string.app_name)
                    binding.imgLogoHeading.visibility = View.VISIBLE
                    binding.btnFilterData.visibility = View.GONE
                    binding.txtHeading.setTextColor(ContextCompat.getColor(this, R.color.gold))
                }
                1 -> {
                    changeFragments(SavedCoinsFragment())
                    binding.imgLogoHeading.visibility = View.GONE
                    binding.txtHeading.text = getString(R.string.saved_coins)
                    binding.btnFilterData.visibility = View.VISIBLE
                    binding.txtHeading.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
                else -> {
                    changeFragments(ProfileFragment())
                    binding.imgLogoHeading.visibility = View.GONE
                    binding.txtHeading.text =getString(R.string.profile)
                    binding.btnFilterData.visibility = View.GONE
                    binding.txtHeading.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
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