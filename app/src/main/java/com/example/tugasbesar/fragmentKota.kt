package com.example.tugasbesar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.entity.kota

class fragmentKota : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_kota, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVKotaAdapter = RVKotaAdapter(kota.listofnamaKota)
        val rvTempatWisataAdapter : RecyclerView = view.findViewById(R.id.rv_kota)
        rvTempatWisataAdapter.layoutManager = layoutManager
        rvTempatWisataAdapter.setHasFixedSize(true)
        rvTempatWisataAdapter.adapter = adapter
    }
}