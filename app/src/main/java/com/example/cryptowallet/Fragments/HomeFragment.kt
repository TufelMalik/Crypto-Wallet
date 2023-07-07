package com.example.cryptowallet.Fragments

import CoinAdapter
import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.Constants
import com.example.cryptowallet.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var dataList: MutableList<String>
    private lateinit var filteredList: MutableList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        searchInRecyclerView()
        getCurrecyList()


        return binding.root
    }



    //  Search Coins in RecylerView....
    private fun searchInRecyclerView() {


    }

    private fun getCurrecyList() {
        binding.homeProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstace().create(ApiInterface::class.java).getMarketData(Constants.LAST_URL)

            withContext(Dispatchers.Main) {

                if (result != null) {
                    binding.homeProgressBar.visibility = View.GONE
                    binding.homeRecyclerView.adapter = CoinAdapter(requireContext(), result.body()!!.data.cryptoCurrencyList)
                   // Log.d("Tufel", result.body()!!.data.cryptoCurrencyList.toString())
                    setRVLayoutOrientationManger(resources.configuration.orientation)


                }
            }

        }
    }

    private fun setRVLayoutOrientationManger(orientation: Int) {
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
