package com.example.tugasbesar.RV

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.DetailsActivity
import com.example.tugasbesar.R
import com.example.tugasbesar.addTiket
import com.example.tugasbesar.entity.itemList
import java.util.*
import kotlin.collections.ArrayList

class ItemAdapter(private var itemList: List<itemList>, context: Context) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>(), Filterable {
    private var filteredItemList: MutableList<itemList>
    private val context:Context

    init {
        filteredItemList = ArrayList(itemList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredItemList.size
    }

    fun setItemList(mahasiswaList: Array<itemList>){
        this.itemList = mahasiswaList.toList()
        filteredItemList = mahasiswaList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredItemList[position]
        holder.tvName.text = item.name
        holder.tvAlamat.text = item.alamat
        holder.tvRating.text = item.rating
        holder.tvPrice.text = item.price

        holder.btnMore.setOnClickListener {
            val intent = Intent(holder.btnMore.context, DetailsActivity::class.java)
            holder.btnMore.context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<itemList> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(itemList)
                }else{
                    for(item in itemList){
                        if(item.name.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        )filtered.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                for (i in filtered){
                    println(i.name)
                }
                return filterResults
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredItemList.clear()
                filteredItemList.addAll(filterResults.values as List<itemList>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvName : TextView
        var tvAlamat : TextView
        var tvRating : TextView
        var tvPrice : TextView
        var btnMore : ImageButton
        var cvItem : CardView

        init {
            tvName = itemView.findViewById(R.id.tv_name)
            tvAlamat = itemView.findViewById(R.id.tv_alamat)
            tvRating = itemView.findViewById(R.id.tv_rating)
            tvPrice = itemView.findViewById(R.id.tv_price)
            btnMore = itemView.findViewById(R.id.btn_more)
            cvItem = itemView.findViewById(R.id.cv_item)
        }
    }
}