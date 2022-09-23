package com.example.tugasbesar.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.tugasbesar.R
import com.example.tugasbesar.MainAdapterProfile
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FragmentProfile : Fragment() {
    lateinit var MainAdapterProfile: MainAdapterProfile
    val db by lazy { UserDB(requireContext(). applicationContext) }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        loadData()
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }
    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            val User = db.noteDao().getNotes()
            Log.d("MainActivity","dbResponse: $User")
            withContext(Dispatchers.Main){
                MainAdapterProfile.setData( User )
            }
        }
    }
    companion object {

    }
}