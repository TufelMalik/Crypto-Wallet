package com.example.cryptowallet

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.TimePicker
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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private val binding by lazy{ActivityMainBinding.inflate(layoutInflater)}
    private val auth = FirebaseAuth.getInstance()
    private lateinit var  db : DatabaseReference


    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar!!.hide()

        binding.noInternetAnimationHome.isVisible = false
        binding.btnFilterData.isVisible = false
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


        binding.btnFilterData.setOnClickListener {
            filterSavedCoinList()
        }

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

    private fun filterSavedCoinList() {
        val dialog = AlertDialog.Builder(this@MainActivity)
        val dialogView = layoutInflater.inflate(R.layout.filter_dialog,null)
        dialog.setView(dialogView)
        val startTime : TextView = dialogView.findViewById(R.id.sortByTimeStart)
        val endTime : TextView = dialogView.findViewById(R.id.sortByTimeEnd)
        val startDate : TextView = dialogView.findViewById(R.id.sortByDateStart)
        val endDate : TextView = dialogView.findViewById(R.id.sortByDateEnd)
        dialog.show()

        startTime.setOnClickListener {
            showTimePicker(startTime)
        }
        endTime.setOnClickListener {
            showTimePicker(endTime)
        }
        startDate.setOnClickListener {
            showDatePicker(startDate)
        }
        endDate.setOnClickListener {
            showDatePicker(endDate)
        }


    }

    private fun showDatePicker(str : TextView) {
        val cal  = Calendar.getInstance()
        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this@MainActivity,
            {_,selectedYear,selectedMonth,selectedDayOfMonth->
                val formatedDate = String.format(Locale.getDefault(),"%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDayOfMonth)
                str.text = formatedDate
            },year,month,dayOfMonth
        )
        datePicker.show()
    }

    private fun showTimePicker(str : TextView) {
        val hour = 0
        val minute = 0

        val timePicker = TimePickerDialog(
            this@MainActivity,
            { _: TimePicker, selectedHour: Int, selectedMinute: Int ->
                val selectedTime = String.format(Locale.getDefault(), "%02d:%02d", selectedHour, selectedMinute)
                    str.text = selectedTime
            },hour,minute,false
        )

        timePicker.show()

    }


    private fun sortSavedCoinList(){
        val savedTimeList : ArrayList<String>
        savedTimeList = ArrayList()
        db = FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)
        db.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val time = snap.child("timeStamp").getValue(SaveCoinsModel::class.java)!!.timeStamp
                    savedTimeList.add(time)
                }
                sortListByDateAndTime(savedTimeList)
            }
            override fun onCancelled(error: DatabaseError) {
                Log.d("MainActivity","Error is in MainActivity:  "+error.message.toString())
            }
        })
    }

    private fun sortListByDateAndTime(savedTimeList: ArrayList<String>) {
        // https://chat.openai.com/share/7063066e-0273-476c-a818-86bf6df39735
    }


    private fun changeFragments(frag : Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer,frag)
            .addToBackStack(null)
            .commit()
    }
}