package com.example.tugasbesar.RV

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.MapsActivity
import com.example.tugasbesar.R
import com.example.tugasbesar.Tokyo
import com.example.tugasbesar.entity.tempatWisataTokyo

class RVTempatWisataAdapter(private val data : Array<tempatWisataTokyo>) : RecyclerView.Adapter<RVTempatWisataAdapter.viewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_item_tempat, parent, false)
        return viewHolder(itemView)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val currentItem = data[position]
        holder.tvName.text = currentItem.name
        holder.tvAlamat.text = currentItem.alamat
        holder.tvRating.text = currentItem.rating.toString()
        holder.seeMore.setOnClickListener(){
            val intent = Intent(holder.itemView.context, MapsActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("Nama",currentItem.name)
            mBundle.putString("Alamat",currentItem.alamat)
            mBundle.putDouble("latitude",currentItem.latitude)
            mBundle.putDouble("longtitude",currentItem.longtitude)
            intent.putExtra("Location",mBundle)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val tvName : TextView = itemView.findViewById(R.id.tv_namaTempat)
        val tvAlamat : TextView = itemView.findViewById(R.id.tv_alamat)
        val tvRating : TextView = itemView.findViewById(R.id.tv_rating)
        val seeMore : TextView = itemView.findViewById(R.id.seeMore)
    }

}