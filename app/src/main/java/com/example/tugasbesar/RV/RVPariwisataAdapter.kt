package com.example.tugasbesar.RV

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.example.tugasbesar.Tokyo
import com.example.tugasbesar.databinding.RvItemPariwisataBinding
import com.example.tugasbesar.fragment.FragmentTempatWisata
import com.example.tugasbesar.entity.entityPariwisata
import com.example.tugasbesar.itemActivity

class RVPariwisataAdapter(private val data: Array<entityPariwisata>) : RecyclerView.Adapter<RVPariwisataAdapter.MainViewHolder>() {
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var vcity : String
    inner class MainViewHolder (val itemBinding: RvItemPariwisataBinding)
        :RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(task: entityPariwisata){
            itemBinding.itemImagePariwisata.setImageResource(task.nama)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(RvItemPariwisataBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = data[position]
        holder.bindItem(task)
        holder.itemView.setOnClickListener(){
            val id = task.id
            if(id == "Wisata"){
                val intent = Intent(holder.itemView.context, itemActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",vuser)
                mBundle.putString("password",vpass)
                mBundle.putString("city", vcity)
                mBundle.putString("category","Tempat Wisata")
                intent.putExtra("profile",mBundle)
                holder.itemView.context.startActivity(intent)
            }else if(id== "Event"){
                val intent = Intent(holder.itemView.context, itemActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",vuser)
                mBundle.putString("password",vpass)
                mBundle.putString("city", vcity)
                mBundle.putString("category","Event")
                intent.putExtra("profile",mBundle)
                holder.itemView.context.startActivity(intent)
            }else if(id=="Akomodasi"){
                val intent = Intent(holder.itemView.context, itemActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",vuser)
                mBundle.putString("password",vpass)
                mBundle.putString("city", vcity)
                mBundle.putString("category","Akomodasi")
                intent.putExtra("profile",mBundle)
                holder.itemView.context.startActivity(intent)
            }else{
                val intent = Intent(holder.itemView.context, itemActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",vuser)
                mBundle.putString("password",vpass)
                mBundle.putString("city", vcity)
                mBundle.putString("category","Culinary")
                intent.putExtra("profile",mBundle)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun getVariable(user : String,pass : String, city : String){
        vuser = user
        vpass = pass
        vcity = city
    }
}