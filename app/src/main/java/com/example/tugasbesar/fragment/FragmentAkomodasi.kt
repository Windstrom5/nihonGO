package com.example.tugasbesar.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.R
import com.example.tugasbesar.RV.RVAkomodasiAdapter
import com.example.tugasbesar.entity.akomodasiTokyo


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

//    LastAdapter(listOfItems, BR.item)
//    .map<Header>(R.layout.item_header)
//    .map<Point>(R.layout.item_point)
//    .into(recyclerView)
//
//    LastAdapter(listOfItems, BR.item).layout { item, position ->
//        when (item) {
//            is Header -> if (position == 0) R.layout.item_header_first else R.layout.item_header
//            else -> R.layout.item_point
//        }
//    }.into(recyclerView)

}