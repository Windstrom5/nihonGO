package com.example.tugasbesar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.databinding.RvItemKotaBinding
import com.example.tugasbesar.task.TaskKota

class MainAdapterKota (val taskList: List<TaskKota>):RecyclerView.Adapter<MainAdapterKota.MainViewHolder>(){
    inner class MainViewHolder (val itemBinding: RvItemKotaBinding)
        :RecyclerView.ViewHolder(itemBinding.root){
        fun bindItem(task: TaskKota){
            itemBinding.itemImage.id = task.nama
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(RvItemKotaBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val task = taskList[position]
        holder.bindItem(task)
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}