package com.example.cryptowallet.Fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Adapters.CoinAdapter
import com.example.cryptowallet.DataClasses.CoinDataClass
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        getCurrecyList()
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun getCurrecyList() {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData()

            withContext(Dispatchers.Main) {

                if (result != null) {

                    binding.homeRecyclerView.adapter = CoinAdapter(requireContext(), result.body()!!.data.cryptoCurrencyList)
                    Log.d("Tufel", result.body()!!.data.cryptoCurrencyList.toString())
                    setRVLayoutManger(resources.configuration.orientation)


                }
            }

        }
    }

    private fun setRVLayoutManger(orientation: Int) {
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            binding.homeRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        }else if(orientation == Configuration.ORIENTATION_PORTRAIT){
            binding.homeRecyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }


}
