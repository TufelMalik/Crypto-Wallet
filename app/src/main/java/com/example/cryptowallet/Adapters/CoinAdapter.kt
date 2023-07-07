import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptowallet.Activitys.DetailedActivity
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R

class CoinAdapter(private val context: Context, private val coinList: List<CryptoCurrency>) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {

    inner class CoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinImg: ImageView = itemView.findViewById(R.id.idCoinImageLay)
        val coinName: TextView = itemView.findViewById(R.id.idCoinNameLay)
        val coinSortName: TextView = itemView.findViewById(R.id.idCoinSortNameLay)
        val coinLivePrice: TextView = itemView.findViewById(R.id.idCoinLivePriceLay)
        val coinFavCB: CheckBox = itemView.findViewById(R.id.idSaveCoinLay)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.home_recycler_layout, parent, false)
        return CoinViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CoinViewHolder, position: Int) {
        val item = coinList[position]

        holder.coinName.text = item.name
        holder.coinSortName.text = item.symbol
        holder.coinLivePrice.text = item.quotes[0].price.toString()
        holder.coinFavCB.isChecked = item.isChecked
        holder.coinFavCB.setOnCheckedChangeListener(null)
        holder.coinFavCB.setOnCheckedChangeListener { _, isChecked ->
            item.isChecked = isChecked
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailedActivity::class.java)
            val coinId = item.id
            intent.putExtra("coinID", coinId)
            context.startActivity(intent)
        }

        Glide.with(context)
            .load("https://s2.coinmarketcap.com/static/img/coins/64x64/${item.id}.png")
            .thumbnail(Glide.with(context).load(R.drawable.loading1))
            .into(holder.coinImg)

        if (item.quotes[0].percentChange1h > 0) {
            holder.coinLivePrice.setTextColor(context.resources.getColor(R.color.green))
            holder.coinLivePrice.text = "+ ${String.format("%.2f", item.quotes[0].percentChange1h)} %"
        } else {
            holder.coinLivePrice.setTextColor(context.resources.getColor(R.color.red))
            holder.coinLivePrice.text = " ${String.format("%.2f", item.quotes[0].percentChange1h)} %"
        }
    }

    override fun getItemCount(): Int {
        return coinList.size
    }
}
