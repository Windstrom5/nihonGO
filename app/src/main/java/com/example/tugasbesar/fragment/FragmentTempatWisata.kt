package com.example.tugasbesar.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.R
import com.example.tugasbesar.RV.RVTempatWisataAdapter
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.models.TempatWisata
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_tempat_wisata.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class FragmentTempatWisata : Fragment() {
    private var srTempatWisata: SwipeRefreshLayout?= null
    private var adapter: RVTempatWisataAdapter? = null
    private var svTempatWisata: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val city = requireArguments().getString("city")!!
        queue= Volley.newRequestQueue(requireContext())
        Log.d("Apa Coba",city)
        srTempatWisata = view?.findViewById(R.id.sr_tempatwisata)
        svTempatWisata = view?.findViewById(R.id.rv_tempatWisata)
        srTempatWisata?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allWisata(city) })
        svTempatWisata?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }
            override fun onQueryTextChange(s: String): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })
        return inflater.inflate(R.layout.fragment_tempat_wisata, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val layoutManager = LinearLayoutManager(context)
        val adapter = RVTempatWisataAdapter(ArrayList(),requireContext())
        val rvTempatWisataAdapter : RecyclerView = view.findViewById(R.id.rv_tempatWisata)
        rvTempatWisataAdapter.layoutManager = layoutManager
        rvTempatWisataAdapter.setHasFixedSize(true)
        rvTempatWisataAdapter.adapter = adapter
    }

    companion object {

    }

    private fun allWisata(city: String){
        sr_tempatwisata!!.isRefreshing=true
        val StringRequest: StringRequest = object : StringRequest(Method.GET,tempatWisataApi.GET_BY_NAMA_URL + city,
            Response.Listener { response->
                val gson = Gson()
                val tempatWisata: Array<TempatWisata> = gson.fromJson(response,Array<TempatWisata>::class.java)
                adapter!!.setTempatWisataList(tempatWisata)
                adapter!!.filter.filter(svTempatWisata!!.query)
                srTempatWisata!!.isRefreshing=false
                if(!tempatWisata.isEmpty()){
                    Toast.makeText(getContext(),"Data Berhasil Diambil", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(getContext(),"Data Kosong", Toast.LENGTH_SHORT).show()
                }
            }, Response.ErrorListener { error->
                srTempatWisata!!.isRefreshing=false
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        getContext(),
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(getContext(),e.message, Toast.LENGTH_SHORT).show()
                }
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(StringRequest)
    }
}