package com.example.tugasbesar

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.room.User
import com.example.tugasbesar.R
import kotlinx.android.synthetic.main.activity_main_adapter_profile.view.*
import kotlinx.android.synthetic.main.activity_profile.view.*

class MainAdapterProfile (private val notes: ArrayList<User>, private val listener: OnAdapterListener) : RecyclerView.Adapter<MainAdapterProfile.NoteViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.activity_main_adapter_profile,parent, false)
        )
    }
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = notes[position]
        holder.view.usernameView.text = note.username
        holder.view.emailView.text = note.email
        holder.view.tglView.text = note.tanggallahir
        holder.view.tlpView.text = note.noTelp
//        holder.view.text_title.setOnClickListener{
//            listener.onClick(note)
//        }
//        holder.view.icon_edit.setOnClickListener {
//            listener.onUpdate(note)
//        }
//        holder.view.icon_delete.setOnClickListener {
//            listener.onDelete(note)
//        }
    }
    override fun getItemCount() = notes.size
    inner class NoteViewHolder( val view: View) :
        RecyclerView.ViewHolder(view)
    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<User>){
        notes.clear()
        notes.addAll(list)
        notifyDataSetChanged()
    }
    interface OnAdapterListener {
        fun onClick(note: User)
        fun onUpdate(note: User)
        fun onDelete(note: User)
    }

//    class viewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        val userText: TextView = itemView.findViewById(R.id.usernameView)
//        val emailText: TextView = itemView.findViewById(R.id.emailView)
//        val tglText: TextView = itemView.findViewById(R.id.tglView)
//        val tlpText: TextView = itemView.findViewById(R.id.tlpView)
//    }
}
