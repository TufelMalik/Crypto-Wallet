package com.example.cryptowallet.Fragments


import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.airbnb.lottie.LottieAnimationView
import com.example.cryptowallet.API.ApiInterface
import com.example.cryptowallet.API.ApiUtilities
import com.example.cryptowallet.Classes.Tufel
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R
import com.example.cryptowallet.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.shashank.sony.fancytoastlib.FancyToast
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
        binding.homeRecyclerView.setBackgroundColor(resources.getColor(R.color.black))
        binding.backHomeFragment.setBackgroundColor(resources.getColor(R.color.black))
        binding.homeProgressBar.visibility = View.GONE
        return binding.root
    }




    lateinit var searchText : String
    private fun searchCoinFromList() {
        binding.searchViewHome.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
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
        if (data.isEmpty()) {
            binding.backHomeFragment.setBackgroundColor(resources.getColor(R.color.black))
            binding.homeRecyclerView.visibility = View.GONE
            binding.txtResult.visibility = View.VISIBLE
            binding.txtResult.text= getString(R.string.coin_not_found)
            binding.notFoundAnimationHome.visibility = View.VISIBLE
        } else {
            binding.homeRecyclerView.visibility = View.VISIBLE
            binding.notFoundAnimationHome.visibility = View.GONE
            adapter.updateData(data)
        }
    }

    private fun callApiGetData() {
        binding.homeProgressBar.visibility = View.VISIBLE
        lifecycleScope.launch(Dispatchers.IO) {
            val result = ApiUtilities.getInstace().create(ApiInterface::class.java)
            withContext(Dispatchers.Main) {
                dataList = result.getMarketData().body()!!.data.cryptoCurrencyList
                if (dataList != null) {
                    binding.homeRecyclerView.setBackgroundColor(resources.getColor(R.color.black))
                    binding.homeProgressBar.visibility = View.GONE
                    adapter = CoinAdapter(requireContext(), dataList)
                    binding.homeRecyclerView.adapter = adapter
                    Log.d("Tufel", dataList.toString())
                    Tufel.setRVLayoutOrientationManger(
                        resources.configuration.orientation,
                        requireContext(),
                        binding.homeRecyclerView
                    )
                }
            }
        }
    }


    private fun checkInternet() {
        try {
            if (Tufel.isOnline(requireContext())) {
                binding.notFoundAnimationHome.visibility = View.GONE
                callApiGetData()
                searchCoinFromList()
            } else {
                FancyToast.makeText(
                    requireContext(), "Offline", FancyToast.LENGTH_SHORT,
                    FancyToast.ERROR, false
                ).show()
                val animationView = binding.notFoundAnimationHome
                binding.homeBanner.visibility = View.GONE
                val offlineAnimationResId = R.raw.no_internet
                updateLottieAnimation(animationView, offlineAnimationResId)
            }
        }catch (e : Exception){
            FancyToast.makeText(requireContext(),"Something went wrong !!!",FancyToast.LENGTH_SHORT,
                FancyToast.ERROR,false).show()
        }
    }


    private fun updateLottieAnimation(animationView: LottieAnimationView, rawResId: Int) {
        animationView.setAnimation(rawResId)
        animationView.playAnimation()
        binding.txtResult.visibility = View.VISIBLE
        binding.txtResult.text= getString(R.string.no_internet)
        binding.notFoundAnimationHome.visibility = View.VISIBLE
    }
    override fun onResume() {
        super.onResume()
        checkInternet()
    }
}