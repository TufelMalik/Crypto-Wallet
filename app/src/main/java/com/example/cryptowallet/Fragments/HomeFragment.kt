package com.example.cryptowallet.Fragments

import CoinAdapter
import CoinViewModel
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.lifecycle.lifecycleScope
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.CoinViewModelFactory
import com.example.cryptowallet.Classes.HomeViewModel
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.databinding.FragmentHomeBinding
import com.example.cryptowallet.repository.CoinRepository
import com.example.cryptowallet.room.CoinsDao
import com.example.cryptowallet.room.CoinsDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.checkerframework.checker.index.qual.LengthOf

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var userId : String
    private lateinit var coinViewModel : CoinViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId= auth.currentUser!!.uid

//        val coinDao = CoinsDatabase.getInstance(requireContext()).coinsDao()
//        val repository = CoinRepository(coinDao)
//        coinViewModel = ViewModelProvider(this, CoinViewModelFactory(repository)).get(CoinViewModel::class.java)


        return binding.root
    }

//
//    private fun getDataFromDB() {
//        database.getReference("FavCoin").child(userId).addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                for (snapshot1 in snapshot.children) {
//                    val coins = snapshot1.getValue(Int::class.java) // Retrieve coin ID as Int
//                    lifecycleScope.launch(Dispatchers.Main) {
//                        val data = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData()
//                            binding.homeProgressBar.visibility = View.GONE
//                            val coinList = data.body()!!.data.cryptoCurrencyList
//                             Log.d("Tufe", coins.toString())
//                            homeViewModel.currencyList.observe(viewLifecycleOwner){
//                                binding.homeRecyclerView.adapter = CoinAdapter(requireContext(), coinList)
//
//                            Tufel.setRVLayoutOrientationManger(resources.configuration.orientation,requireContext(),binding.homeRecyclerView)
//                        }
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // Handle onCancelled event
//            }
//        })
//    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        getDataFromDB()
//        coinViewModel.getCoinData().observe(viewLifecycleOwner, Observer {
//            Log.d("@@@",it.toString())
//        })
//        callApiGetData()
    }

    private fun callApiGetData() {

        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstace().create(ApiInterface::class.java)
            withContext(Dispatchers.Main) {
                val coinData = result.getMarketData().body()!!.data.cryptoCurrencyList
                if (coinData != null) {
                    binding.homeRecyclerView.adapter = CoinAdapter(requireContext(), coinData)
                    Log.d("Tufel", coinData.toString())
//                    coinData?.let {
//                        // Save data in Room
//                        for (coin in coinData) {
//                            coinViewModel.insertCoins(coin)
//                        }
                        Tufel.setRVLayoutOrientationManger(
                            resources.configuration.orientation,
                            requireContext(),
                            binding.homeRecyclerView
                        )
                    }
                }

            }
    }

    override fun onResume() {
        super.onResume()
//        getDataFromDB()
//        Log.d("Tufel","OnResume")
    }

}
