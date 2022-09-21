package com.example.tugasbesar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.databinding.RvItemKotaBinding
import com.example.tugasbesar.entity.kota

class RVKotaAdapter(private val data: Array<kota>) : RecyclerView.Adapter<RVKotaAdapter.MainViewHolder>() {
    inner class MainViewHolder (val itemBinding: RvItemKotaBinding)
        :RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(task: kota){
            itemBinding.itemImage.setImageResource(task.nama)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(RvItemKotaBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = data[position]
        holder.bindItem(task)
        holder.itemView.setOnClickListener(){
            val id = task.id
            if(id == "Tokyo"){
//                val intent = Intent(RVKotaAdapter,Tokyo::class.java)
//                intent.putExtra("Tokyo",task.nama)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}