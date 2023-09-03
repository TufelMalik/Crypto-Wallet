package com.example.cryptowallet.Fragments

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptowallet.Activitys.DetailedActivity
import com.example.cryptowallet.Classes.Tufel.saveFavCoinstoDB
import com.example.cryptowallet.Classes.Tufel.unSaveCointoDB
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CoinAdapter(
    private val context: Context,
    private var coinList: List<CryptoCurrency>
) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {
    private val checkboxStates = MutableList(coinList.size) { false }

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinImg: ImageView = itemView.findViewById(R.id.idCoinImageLay)
        val coinName: TextView = itemView.findViewById(R.id.idCoinNameLay)
        val coinSortName: TextView = itemView.findViewById(R.id.idCoinSortNameLay)
        val coinLivePrice: TextView = itemView.findViewById(R.id.idCoinLivePriceLay)
        val coinFavCB: CheckBox = itemView.findViewById(R.id.idSaveCoinLay)
        val coinChangePrice: ImageView = itemView.findViewById(R.id.idCoinPriceChangeLay)

        init {
            coinFavCB.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = coinList[position]
                    item.isChecked = isChecked

                    if (isChecked) {
                        saveFavCoinstoDB(coinFavCB,item.name!!,item.id)
                    } else {
                        unSaveCointoDB(item.name!!)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.home_recycler_layout, parent, false)
        return CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val item = coinList[position]

        holder.coinName.text = item.name
        holder.coinSortName.text = item.symbol
        holder.coinLivePrice.text = item.quotes[0].price.toString()
        holder.coinFavCB.isChecked = item.isChecked
        isCheckBoxIsSaved(item,holder.coinFavCB)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailedActivity::class.java)
            intent.putExtra("data", item.id)
            context.startActivity(intent)
        }

        Glide.with(context)
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png")
            .thumbnail(Glide.with(context).load(R.drawable.loading13))
            .into(holder.coinImg)

        val percentChange1h = item.quotes[0].percentChange1h
        holder.coinLivePrice.setTextColor(
            ContextCompat.getColor(
                context,
                if (percentChange1h > 0) R.color.lime else R.color.red
            )
        )
        holder.coinLivePrice.text =
            if (percentChange1h > 0) "+ " + String.format("%.2f %%", percentChange1h) else String.format(
                "%.2f %%",
                percentChange1h
            )

        Glide.with(context)
            .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${item.id}.png")
            .thumbnail(Glide.with(context).load(R.drawable.loading13))
            .into(holder.coinChangePrice)
    }

        private fun isCheckBoxIsSaved(data: CryptoCurrency, coinFavCB: CheckBox) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            var savedTime = ""
            val userId = currentUser.uid
            val favCoinRef = FirebaseDatabase.getInstance().getReference("FavCoin").child(userId)
            favCoinRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val savedCoins = ArrayList<Long>()
                    for (snap in snapshot.children) {
                        val savedCoinsId = snap.child("coinId").getValue(Long::class.java)
                        savedTime = snap.child("timeStamp").getValue(String::class.java).toString()
                        savedCoinsId?.let { savedCoins.add(it) }
                    }
                    Log.d("CoinAdapter", "Saved Coins: $savedCoins")
                    if (savedCoins.contains(data.id)) {
                         coinFavCB.isChecked = true

                    }
                    Log.d("CoinAdapter", "Matched Coin is Checked Now: $savedCoins")
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("CoinAdapter", "Database error: $error")
                }
            })
        }
    }



    override fun getItemCount() = coinList.size

    fun updateData(data: List<CryptoCurrency>) {
        coinList = data
        notifyDataSetChanged()
    }


}
