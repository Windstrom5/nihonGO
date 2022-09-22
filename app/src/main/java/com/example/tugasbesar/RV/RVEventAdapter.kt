package com.example.tugasbesar.RV

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.R
import com.example.tugasbesar.entity.eventTokyo

class RVEventAdapter(private val data : Array<eventTokyo>) : RecyclerView.Adapter<RVEventAdapter.viewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_event, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvName.text = currentItem.name
        holder.tvAlamat.text = currentItem.alamat
        holder.tvDate.text = currentItem.tanggal
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName : TextView = itemView.findViewById(R.id.tv_namaAcara)
        val tvAlamat : TextView = itemView.findViewById(R.id.tv_alamat)
        val tvDate : TextView = itemView.findViewById(R.id.tv_date)
    }
}