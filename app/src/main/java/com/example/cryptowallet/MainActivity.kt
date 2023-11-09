package com.example.cryptowallet

import SavedCoinAdapter
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.DataClasses.SaveCoinsModel
import com.example.cryptowallet.Fragments.HomeFragment
import com.example.cryptowallet.Fragments.ProfileFragment
import com.example.cryptowallet.Fragments.SavedCoinsFragment
import com.example.cryptowallet.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var auth: FirebaseAuth
    private var database = FirebaseDatabase.getInstance().reference
    private lateinit var userId: String
    private var startDate1 = ""
    private var endDate1 = ""

    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar!!.hide()

        auth = FirebaseAuth.getInstance()
        userId = auth.currentUser!!.uid
        binding.noInternetAnimationHome.isVisible = false
        binding.btnFilterData.isVisible = false
        if (Tufel.isOnline(this@MainActivity)) {
            FancyToast.makeText(
                this@MainActivity, "Online", FancyToast.LENGTH_SHORT,
                FancyToast.SUCCESS, false
            ).show()
            binding.noInternetAnimationHome.visibility = View.VISIBLE
        } else {
            binding.noInternetAnimationHome.visibility = View.GONE
        }

        binding.txtHeading.text = getText(R.string.app_name)
        binding.imgLogoHeading.visibility = View.VISIBLE
        binding.txtHeading.setTextColor(ContextCompat.getColor(this, R.color.gold))

        binding.btnFilterData.setOnClickListener {
            filterSavedCoinList()
        }

        // By Default Home Fragment
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
                    binding.txtHeading.text = getString(R.string.profile)
                    binding.btnFilterData.visibility = View.GONE
                    binding.txtHeading.setTextColor(ContextCompat.getColor(this, R.color.white))
                }
            }
        }
    }

    private fun filterSavedCoinList() {
        val dialog = Dialog(this@MainActivity)
        val dialogView = layoutInflater.inflate(R.layout.filter_dialog,null)
        dialog.setContentView(dialogView)
        val startDate: TextView = dialogView.findViewById(R.id.sortByDateStart)
        val endDate: TextView = dialogView.findViewById(R.id.sortByDateEnd)
        val btnFilter: Button = dialogView.findViewById(R.id.btnFilterDialog)



        startDate.setOnClickListener {
            showDatePicker(startDate, "s")
        }
        endDate.setOnClickListener {
            showDatePicker(endDate, "e")
        }
        btnFilter.setOnClickListener {
            dialog.dismiss()
            sortSavedCoinList(startDate1,endDate1)
            dialog.setCancelable(true) // Dismiss the dialog
            startDate1 = ""
            endDate1 = ""
        }
        dialog.show()
    }

    private fun showDatePicker(str: TextView, s: String) {
        val cal = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this@MainActivity,
            { _, selectedYear, selectedMonth, selectedDayOfMonth ->
                val formattedDate =
                    String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)
                str.text = formattedDate
                if (s.equals("s")) {
                    startDate1 = formattedDate
                } else {
                    endDate1 = formattedDate
                }
            }, year, month, dayOfMonth
        )
        datePicker.show()
    }

    private fun sortSavedCoinList(sDate: String, eDate: String) {
        val savedTimeList: ArrayList<SaveCoinsModel> = ArrayList()
        database = FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snap in snapshot.children) {
                    val savedCoin = snap.getValue(SaveCoinsModel::class.java)
                    savedTimeList.add(savedCoin!!)
                }
                val coinModel = mutableListOf<SaveCoinsModel>()
                for (savedCoin in savedTimeList) {
                    val extractedDate = savedCoin.date
                    val extractedTime = savedCoin.time
                    coinModel.add(SaveCoinsModel(savedCoin.coinId, savedCoin.coinName, extractedTime,extractedDate))
                }
                val filteredCoins = coinModel.filter {
                    val coinDate = it.date.trim() // Trim any extra whitespaces
                    //     Log.d("MainActivity","\n\n\n ............ \n\n\n CoinDate : $coinDate \n\n  ----- StrtDate : ${sDate} ------ EndDate : ${eDate} \n\n\n...Munnu...end\n\n\n\n")
                    coinDate >= sDate && coinDate <= eDate
                }


                if(filteredCoins.isNotEmpty()) {
                    val adapter = SavedCoinAdapter(this@MainActivity, emptyList(), emptyList())
                    adapter.filterDataByDateTime(
                        filteredCoins.ifEmpty { emptyList() }
                    )
                    Log.d("MainActivity", "\n\n\n\n--->adapter ----\n\n $filteredCoins \n\n\n")
                }else{
                    Toast.makeText(this@MainActivity, "Coins Not Found!", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("MainActivity", "Error is in MainActivity:  " + error.message.toString())
            }
        })
    }



    private fun changeFragments(frag: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, frag)
            .addToBackStack(null)
            .commit()
    }
}