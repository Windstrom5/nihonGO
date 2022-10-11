package com.example.tugasbesar.RV

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.R
import com.example.tugasbesar.entity.akomodasiTokyo

class RVAkomodasiAdapter(private val data : Array<akomodasiTokyo>) : RecyclerView.Adapter<RVAkomodasiAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_akomodasi, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvName.text = currentItem.name
        holder.tvAlamat.text = currentItem.alamat
        holder.tvRating.text = currentItem.rating.toString()
        holder.more.setOnClickListener(){
            val name = currentItem.name
            if (name == "Park Hyatt Tokyo"){

            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName : TextView = itemView.findViewById(R.id.tv_namaTempat)
        val tvAlamat : TextView = itemView.findViewById(R.id.tv_alamat)
        val tvRating : TextView = itemView.findViewById(R.id.tv_rating)
        val more : TextView = itemView.findViewById(R.id.seeMore)
    }
}