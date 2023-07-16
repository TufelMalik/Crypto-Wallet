import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

class CoinAdapter(
    private val context: Context,
    private var coinList: List<CryptoCurrency>,
) :
    RecyclerView.Adapter<CoinAdapter.CoinViewHolder>() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: DatabaseReference

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
                        saveDataOnDB(item.name, item.id)
                    } else {
                        removeDataFromDB(item.id)
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

        // ... Rest of your onBindViewHolder code ...

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

        Glide.with(context)
            .load("https://s3.coinmarketcap.com/generated/sparklines/web/7d/usd/${item.id}.png")
            .thumbnail(Glide.with(context).load(R.drawable.loading13))
            .into(holder.coinChangePrice)
    }

    override fun getItemCount() = coinList.size

    fun updateData(data: List<CryptoCurrency>) {
        coinList = data
        notifyDataSetChanged()
    }

    private fun saveDataOnDB(name: String?, id: Long) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseDatabase.getInstance().getReference("FavCoin").child(auth.currentUser!!.uid)

        db.child(name!!)
            .setValue(id)
            .addOnCompleteListener {
                Toast.makeText(context, "$name Coin Saved", Toast.LENGTH_SHORT).show()
            }
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
