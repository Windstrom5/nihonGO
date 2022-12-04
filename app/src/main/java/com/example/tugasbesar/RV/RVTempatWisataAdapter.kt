package com.example.tugasbesar.RV

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.tugasbesar.MapsActivity
import com.example.tugasbesar.R
import com.example.tugasbesar.models.TempatWisata
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*
import kotlin.collections.ArrayList

class RVTempatWisataAdapter(private var tempatWisataList: List<TempatWisata>,context: Context) :
    RecyclerView.Adapter<RVTempatWisataAdapter.viewHolder>(), Filterable {
    private var filteredTempatWisataList: MutableList<TempatWisata>
    private val context:Context
    init {
        filteredTempatWisataList = ArrayList(tempatWisataList)
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.rv_item_tempat, parent, false)
        return viewHolder(view)
    }
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
//        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_tempat, parent, false)
//        return viewHolder(itemView)
//    }

    override fun getItemCount(): Int {
        return filteredTempatWisataList.size
    }

    fun setTempatWisataList(wisataList: Array<TempatWisata>){
        this.tempatWisataList = wisataList.toList()
        filteredTempatWisataList = wisataList.toMutableList()
    }

//    override fun onBindViewHolder(holder: viewHolder, position: Int) {
//        val currentItem = data[position]
//        holder.tvName.text = currentItem.name
//        holder.tvAlamat.text = currentItem.alamat
//        holder.tvRating.text = currentItem.rating.toString()
//        holder.seeMore.setOnClickListener(){
//            val intent = Intent(holder.itemView.context, MapsActivity::class.java)
//            val mBundle = Bundle()
//            mBundle.putString("Nama",currentItem.name)
//            mBundle.putString("Alamat",currentItem.alamat)
//            mBundle.putDouble("latitude",currentItem.latitude)
//            mBundle.putDouble("longtitude",currentItem.longtitude)
//            intent.putExtra("Location",mBundle)
//            holder.itemView.context.startActivity(intent)
//        }
//    }
    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val wisata = filteredTempatWisataList[position]
        holder.tvName.text = wisata.name
        holder.tvAlamat.text = wisata.alamat
        holder.tvRating.text = wisata.rating.toString()
    }
//    override fun getItemCount(): Int {
//        return data.size
//    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<TempatWisata> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(tempatWisataList)
                }else{
                    for(wisata in tempatWisataList){
                        if(wisata.name.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        )filtered.add(wisata)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults?) {
                filteredTempatWisataList.clear()
                filteredTempatWisataList.addAll(filterResults?.values as List<TempatWisata>)
                notifyDataSetChanged()
            }
        }
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName : TextView = itemView.findViewById(R.id.tv_namaTempat)
        val tvAlamat : TextView = itemView.findViewById(R.id.tv_alamat)
        val tvRating : TextView = itemView.findViewById(R.id.tv_rating)
        val seeMore : TextView = itemView.findViewById(R.id.seeMore)
    }
}