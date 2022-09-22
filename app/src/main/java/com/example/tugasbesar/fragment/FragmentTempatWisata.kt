package com.example.tugasbesar.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.R
import com.example.tugasbesar.RV.RVTempatWisataAdapter
import com.example.tugasbesar.entity.tempatWisataTokyo

class FragmentTempatWisata : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tempat_wisata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVTempatWisataAdapter = RVTempatWisataAdapter(tempatWisataTokyo.listOftempatWisata)
        val rvTempatWisataAdapter : RecyclerView = view.findViewById(R.id.rv_tempatWisata)
        rvTempatWisataAdapter.layoutManager = layoutManager
        rvTempatWisataAdapter.setHasFixedSize(true)
        rvTempatWisataAdapter.adapter = adapter
    }

    companion object {

    }
}