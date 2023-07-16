package com.example.cryptowallet.Fragments

import CoinAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var database : FirebaseDatabase
    private lateinit var userId : String
    private lateinit var adapter : CoinAdapter
//    private lateinit var coinViewModel : CoinViewModel
    private lateinit var dataList: List<CryptoCurrency>



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        userId= auth.currentUser!!.uid
        dataList = listOf()
        binding.homeProgressBar.visibility = View.GONE
        callApiGetData()
        searchCoinFromList()
//        val coinDao = CoinsDatabase.getInstance(requireContext()).coinsDao()
//        val repository = CoinRepository(coinDao)
//        coinViewModel = ViewModelProvider(this, CoinViewModelFactory(repository)).get(CoinViewModel::class.java)


        return binding.root
    }


    lateinit var searchText : String
    private fun searchCoinFromList() {
        binding.searchViewHome.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            @SuppressLint("SuspiciousIndentation")
            override fun afterTextChanged(p0: Editable?) {
              searchText = p0.toString().lowercase()
                updateRecyclerView()
            }

        })
    }

    private fun updateRecyclerView() {
        val data = ArrayList<CryptoCurrency>()
        for (item in dataList) {
            val coinName = item.name!!.lowercase(Locale.getDefault())
            val coinSymbol = item.symbol!!.lowercase(Locale.getDefault())
            if (coinName.contains(searchText) || coinSymbol.contains(searchText)) {
                data.add(item)
            }
        }

        // Check if the data list is empty
        if (data.isEmpty()) {
            binding.backHomeFragment.setBackgroundColor(resources.getColor(R.color.black))
            binding.homeRecyclerView.visibility = View.GONE
            binding.notFoundAnimationHome.visibility = View.VISIBLE // Assuming you have a TextView with "No data found" message
        } else {
            binding.homeRecyclerView.visibility = View.VISIBLE
            binding.notFoundAnimationHome.visibility = View.GONE
            adapter.updateData(data)
        }
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






    private fun callApiGetData() {

        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstace().create(ApiInterface::class.java)
            withContext(Dispatchers.Main) {
                dataList = result.getMarketData().body()!!.data.cryptoCurrencyList
                if (dataList != null) {
                    adapter = CoinAdapter(requireContext(), dataList)
                    binding.homeRecyclerView.adapter = adapter
                    Log.d("Tufel", dataList.toString())
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
        binding.notFoundAnimationHome.visibility = View.GONE
    }

}
