package com.example.tugasbesar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tugasbesar.RV.RVKotaAdapter
import com.example.tugasbesar.databinding.ActivityKotaBinding
import com.example.tugasbesar.entity.kota

class kota : AppCompatActivity() {
    var binding : ActivityKotaBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKotaBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val adapter = RVKotaAdapter(kota.listofnamaKota)
        binding?.rvKota?.adapter = adapter
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val layoutManager = LinearLayoutManager(context)
//        val adapter : RVKotaAdapter = RVKotaAdapter(kota.listofnamaKota)
//
//        rvKulinerAdapter.layoutManager = layoutManager
//        rvKulinerAdapter.setHasFixedSize(true)
//        rvKulinerAdapter.adapter = adapter

//    }
}