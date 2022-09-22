package com.example.tugasbesar.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.R
import com.example.tugasbesar.RV.RVEventAdapter
import com.example.tugasbesar.entity.eventTokyo


class FragmentEvent : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_event, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter : RVEventAdapter = RVEventAdapter (eventTokyo.listOfevent)
        val RVEventAdapter  : RecyclerView = view.findViewById(R.id.rv_event)
        RVEventAdapter.layoutManager = layoutManager
        RVEventAdapter .setHasFixedSize(true)
        RVEventAdapter.adapter = adapter
    }

    companion object {

    }
}