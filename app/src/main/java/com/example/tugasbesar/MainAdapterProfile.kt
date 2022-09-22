package com.example.tugasbesar


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.room.User
import kotlinx.android.synthetic.main.fragment_profil.view.*


class MainAdapterProfile (private val Users: ArrayList<User>, private val listener: OnAdapterListener) : RecyclerView.Adapter<MainAdapterProfile.NoteViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        return NoteViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_profil,parent, false)
        )
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val user = Users[position]
        holder.view.usernameView.text = user.username
        holder.view.emailView.text = user.email
        holder.view.tglView.text = user.tanggallahir
        holder.view.tlpView.text = user.noTelp

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

    override fun getItemCount() = Users.size

    inner class NoteViewHolder( val view: View) : RecyclerView.ViewHolder(view)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<User>){
        Users.clear()
        Users.addAll(list)
        notifyDataSetChanged()
    }

    interface OnAdapterListener {
        fun onClick(user: User)
        fun onUpdate(user: User)
        fun onDelete(user: User)
    }
}