import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptowallet.Activitys.DetailedActivity
import com.example.cryptowallet.DataClasses.CryptoCurrency
import com.example.cryptowallet.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class SavedCoinAdapter(
    private val context: Context,
    private var coinList: List<CryptoCurrency>,
) :
    RecyclerView.Adapter<SavedCoinAdapter.SavedCoinViewHolder>() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

    inner class SavedCoinViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coinImg: ImageView = itemView.findViewById(R.id.imgCoinImageFav_layout)
        val coinName: TextView = itemView.findViewById(R.id.txtCoinName_Layout)
        val coinLivePrice: TextView = itemView.findViewById(R.id.txtCoinPrice_layout)
        val coinFavCB: CheckBox = itemView.findViewById(R.id.cbFav_layout)
        val webView : WebView = itemView.findViewById(R.id.detaillChartWebView)
        val detailChangeImageView : ImageView = itemView.findViewById(R.id.detailChangeImageView)


        init {
            coinFavCB.setOnCheckedChangeListener { _, isChecked ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = coinList[position]
                    item.isChecked = true

                  if(!isChecked){
                        removeDataFromDB(item.id)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCoinViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.fav_coins_layout, parent, false)
        return SavedCoinViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: SavedCoinViewHolder, position: Int) {
        val item = coinList[position]

        holder.coinName.text = item.name
        holder.coinLivePrice.text = item.quotes[0].price.toString()
        holder.coinFavCB.isChecked = true

        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailedActivity::class.java)
            Toast.makeText(context, item.id.toString(), Toast.LENGTH_SHORT).show()
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
                if (percentChange1h > 0) R.color.green else R.color.red
            )
        )
        holder.coinLivePrice.text =
            if (percentChange1h > 0) "+ " + String.format("%.2f %%", percentChange1h) else String.format(
                "%.2f %%",
                percentChange1h
            )
        if(item.quotes[0].percentChange1h > 0){
            holder.detailChangeImageView.setImageResource(R.drawable.ic_caret_up)
        }else{
            holder.detailChangeImageView.setImageResource(R.drawable.ic_caret_down)
        }
      setWebView(item.symbol,item.id,holder.webView)


    }

    private fun setWebView(symbol: String?, id: Long, webView : WebView) {
        webView.settings.javaScriptEnabled = true
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE,null)
        webView.loadUrl(getChartUrl(symbol))
    }

    private fun getChartUrl(symbol: String?): String {
       return "https://s.tradingview.com/widgetembed/?frameElementId=tradingview_76d87&symbol=${symbol}USD&interval=15&hidesidetoolbar=1&hidetoptoolbar=1&symboledit=1&saveimage=1&toolbarbg=F1F3F6&studies=[]&hideideas=1&theme=Dark&style=1&timezone=Etc%2FUTC&studies_overrides={}&overrides={}&enabled_features=[]&disabled_features=[]&locale=en&utm_source=coinmarketcap.com&utm_medium=widget&utm_campaign=chart&utm_term=BTCUSDT"

    }

    override fun getItemCount() = coinList.size

    fun updateData(data: List<CryptoCurrency>) {
        coinList = data
        notifyDataSetChanged()
    }



    private fun removeDataFromDB(id: Long) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (id1 in snapshot.children) {
                    val data = id1.getValue(Int::class.java)
                    if (data!!.toLong() == id) {
                        id1.ref.removeValue()
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled if needed
            }
        })
    }
}
