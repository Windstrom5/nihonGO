package com.example.tugasbesar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.entity.akomodasiTokyo
import com.example.tugasbesar.entity.tempatWisataTokyo


class FragmentAkomodasi : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_akomodasi, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVAkomodasiAdapter = RVAkomodasiAdapter(akomodasiTokyo.listOfakomodasi)
        val rvAkomodasiAdapter : RecyclerView = view.findViewById(R.id.rv_akomodasi)
        rvAkomodasiAdapter.layoutManager = layoutManager
        rvAkomodasiAdapter.setHasFixedSize(true)
        rvAkomodasiAdapter.adapter = adapter
    }

    companion object {

    }
}