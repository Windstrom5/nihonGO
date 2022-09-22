package com.example.tugasbesar.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.R
import com.example.tugasbesar.RV.RVKulinerAdapter
import com.example.tugasbesar.entity.kulinerTokyo


class FragmentKuliner : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kuliner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVKulinerAdapter = RVKulinerAdapter(kulinerTokyo.listOfkuliner)
        val rvKulinerAdapter : RecyclerView = view.findViewById(R.id.rv_kuliner)
        rvKulinerAdapter.layoutManager = layoutManager
        rvKulinerAdapter.setHasFixedSize(true)
        rvKulinerAdapter.adapter = adapter
    }

    companion object {

    }
}