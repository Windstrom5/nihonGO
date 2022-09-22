package com.example.tugasbesar.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasbesar.MainAdapterProfile
import com.example.tugasbesar.R

class FragmentProfile : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
//        MainAdapterProfile.layoutManager = layoutManager
//        RVEventAdapter .setHasFixedSize(true)
//        RVEventAdapter.adapter = adapter
    }

    companion object {

    }
}