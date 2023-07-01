package com.example.cryptowallet.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptowallet.DataClasses.CoinDataClass
import com.example.cryptowallet.R

class CoinAdapter(private val coinList: List<CoinDataClass>) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinImg: ImageView = itemView.findViewById(R.id.idCoinImageLay)
        val coinName: TextView = itemView.findViewById(R.id.idCoinNameLay)
        val coinSortName: TextView = itemView.findViewById(R.id.idCoinSortNameLay)
        val coinLivePrice: TextView = itemView.findViewById(R.id.idCoinLivePriceLay)
        val coinDetails: TextView = itemView.findViewById(R.id.idCoinDetailLay)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_recycler_layout, parent, false)
        return CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val item = coinList[position]
        holder.coinImg.setImageResource(item.coinImg ?: R.drawable.ic_fb_laugh)
        holder.coinName.text = item.coinName
        holder.coinSortName.text = item.coinSortName
        holder.coinLivePrice.text = item.coinPrice
        holder.coinDetails.text = item.coinDetials
        // Set other properties as needed
    }

    override fun getItemCount(): Int {
        return coinList.size
    }
}
