package com.example.cryptowallet.Fragments

import SavedCoinAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.SharedPrefsHelper
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.Classes.Tufel.isOnline
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.DataClasses.SaveCoinsModel
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.FragmentSavedCoinsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.shashank.sony.fancytoastlib.FancyToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SavedCoinsFragment : Fragment() {
    private lateinit var binding: FragmentSavedCoinsBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var userId : String
    private lateinit var sharedPrefsHelper: SharedPrefsHelper
    private lateinit var coinList : ArrayList<CryptoCurrency>
    private lateinit var btnFilter : ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedCoinsBinding.inflate(inflater, container, false)

        // btnFilter = container!!.findViewById(R.id.btnFilterData)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId= auth.currentUser!!.uid
        sharedPrefsHelper = SharedPrefsHelper(requireContext())
        sharedPrefsHelper.setSelectedCoinIds(setOf())
        if (isOnline(requireContext())) {
            getDataFromDB()
        }else{
            binding.itemNotFoundAnimationFavcoins.visibility=  View.VISIBLE
            FancyToast.makeText(requireContext(),"Offline",FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,false).show()
        }


        return binding.root
    }


    private fun getDataFromDB() {
        database.getReference("FavCoin").child(userId).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val res = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData()
                    coinList = ArrayList()
                    withContext(Dispatchers.Main) {
                        val apiDataList = res.body()?.data?.cryptoCurrencyList
                        if (apiDataList != null) {
                            var savedTime = ""
                            for (snapshot1 in snapshot.children) {

                                savedTime = snapshot1.getValue(SaveCoinsModel::class.java)!!.timeStamp
                                val savedCoinsId = snapshot1.getValue(SaveCoinsModel::class.java)?.coinId
                                val matchingCoin = apiDataList.find { it.id == savedCoinsId }
                                matchingCoin?.let {
                                    coinList.add(it)
                                }
                            }
                            try{
                                if (coinList.isNotEmpty()) {
                                    binding.itemNotFoundAnimationFavcoins.visibility = View.GONE
                                } else {
                                    binding.itemNotFoundAnimationFavcoins.visibility = View.VISIBLE
                                }

                                var time = SaveCoinsModel(coinList[0].id,coinList[0].name!!, savedTime)
                                binding.savedCoinsRecyclerView.adapter = SavedCoinAdapter(context!!,coinList,time)
                                binding.savedCoinsRecyclerView.layoutManager = LinearLayoutManager(context)
                            }catch (e: Exception){
                                binding.itemNotFoundAnimationFavcoins.visibility = View.VISIBLE
                                Log.d("SavedCoinsFragment","Error is : "+ e.message.toString() )
                            }
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }



}