package com.example.cryptowallet.Fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptowallet.Adapters.CoinAdapter
import com.example.cryptowallet.DataClasses.CoinDataClass
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: CoinAdapter
    private val coinDataList = mutableListOf<CoinDataClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        addCoinData()
        updateRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = CoinAdapter(coinDataList)
        binding.homeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.homeRecyclerView.adapter = adapter
    }

    private fun addCoinData() {
        val imageList = listOf(
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,
            R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh, R.drawable.ic_fb_laugh,

        )
        val coinName = listOf(
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",

            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5",
            "Coin Name 1", "Coin Name 2", "Coin Name 3", "Coin Name 4", "Coin Name 5"
        )
        val coinPrice = listOf(
            "200.32", "198.32", "52.21", "33.19", "451.22",
            "200.32", "198.32", "52.21", "33.19", "451.22",
            "200.32", "198.32", "52.21", "33.19", "451.22",
            "200.32", "198.32", "52.21", "33.19", "451.22",
            "200.32", "198.32", "52.21", "33.19", "451.22",

            "200.32", "198.32", "52.21", "33.19", "451.22",
            "200.32", "198.32", "52.21", "33.19", "451.22",
            "200.32", "198.32", "52.21", "33.19", "451.22",
            "200.32", "198.32", "52.21", "33.19", "451.22",
            "200.32", "198.32", "52.21", "33.19", "451.22"
        )
        val coinLiveData = listOf(
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",

            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5",
            "Coin Data 1", "Coin Data 2", "Coin Data 3", "Coin Data 4", "Coin Data 5"
        )
        val coinSortName = listOf(
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",

            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5",
            "Coin1", "Coin2", "Coin3", "Coin4", "Coin5"
        )
        val coinDetails = listOf(
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",

            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5",
            "Coin D1", "Coin D2", "Coin D3", "Coin D4", "Coin D5"
        )

        for (i in coinName.indices) {
            val coinData = CoinDataClass(
                coinName[i],
                coinPrice[i],
                imageList[i],
                coinLiveData[i],
                coinSortName[i],
                coinDetails[i]
            )
            coinDataList.add(coinData)
        }
    }




    private fun updateRecyclerView() {
        adapter.notifyDataSetChanged()
    }
}
