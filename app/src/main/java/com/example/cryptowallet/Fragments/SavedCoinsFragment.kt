package com.example.cryptowallet.Fragments

import SavedCoinAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.SharedPrefsHelper
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.DataClasses.CryptoCurrency
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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedCoinsBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId= auth.currentUser!!.uid
        sharedPrefsHelper = SharedPrefsHelper(requireContext())
        sharedPrefsHelper.setSelectedCoinIds(setOf())
        if (Tufel.isOnline(requireContext())) {
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
                            for (snapshot1 in snapshot.children) {
                                val savedCoinsId = snapshot1.getValue(Long::class.java)
                                val matchingCoin = apiDataList.find { it.id == savedCoinsId }
                                matchingCoin?.let {
                                    coinList.add(it)
                                }
                            }

                            if (coinList.isEmpty()) {
                                binding.itemNotFoundAnimationFavcoins.visibility = View.VISIBLE
                            } else {
                                binding.itemNotFoundAnimationFavcoins.visibility = View.GONE
                            }
                            binding.savedCoinsRecyclerView.adapter = SavedCoinAdapter(context!!,coinList)
                            binding.savedCoinsRecyclerView.layoutManager = LinearLayoutManager(context)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }



}