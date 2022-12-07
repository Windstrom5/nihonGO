package com.example.tugasbesar

import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.RV.ItemAdapter
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.entity.itemList
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class itemActivity : AppCompatActivity() {
    private var srItem: SwipeRefreshLayout?= null
    private var adapter: ItemAdapter? = null
    private var svItem: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private lateinit var vuser:String
    private lateinit var vpass:String
    private lateinit var vcity:String
    private lateinit var vcategory:String
    lateinit var mbunlde : Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        getBundle()
        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srItem = findViewById(R.id.sr_item)
        svItem = findViewById(R.id.sv_item)
        srItem?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allItem(vcategory,vcity) })
        svItem?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }
            override fun onQueryTextChange(s: String): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })
        val rvItem = findViewById<RecyclerView>(R.id.rv_item)
        adapter = ItemAdapter(ArrayList(),this)
        rvItem.layoutManager = LinearLayoutManager(this)
        rvItem.adapter= adapter
        allItem(vcategory,vcity)
    }

    private fun allItem(category:String,kota : String){
        if(category == "Wisata"){
            srItem!!.isRefreshing=true
            val StringRequest: StringRequest = object : StringRequest(Method.GET, tempatWisataApi.GET_BY_NAMA_URL + kota,
                Response.Listener { response->
                    val gson = Gson()
                    val item: Array<itemList> = gson.fromJson(response,Array<itemList>::class.java)
                    adapter!!.setItemList(item)
                    adapter!!.filter.filter(svItem!!.query)
                    srItem!!.isRefreshing=false
                    if(!item.isEmpty()){
                        Toast.makeText(this@itemActivity,"Data Berhasil Diambil", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@itemActivity,"Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error->
                    srItem!!.isRefreshing=false
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@itemActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(this@itemActivity,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
            queue!!.add(StringRequest)
        }else if(category == "Event"){
            srItem!!.isRefreshing=true
            val StringRequest: StringRequest = object : StringRequest(Method.GET,tempatWisataApi.GET_BY_NAMA_URL + kota,
                Response.Listener { response->
                    val gson = Gson()
                    val item: Array<itemList> = gson.fromJson(response,Array<itemList>::class.java)
                    adapter!!.setItemList(item)
                    adapter!!.filter.filter(svItem!!.query)
                    srItem!!.isRefreshing=false
                    if(!item.isEmpty()){
                        Toast.makeText(this@itemActivity,"Data Berhasil Diambil", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@itemActivity,"Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error->
                    srItem!!.isRefreshing=false
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@itemActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(this@itemActivity,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
            queue!!.add(StringRequest)
        }

    }

    fun setLoading(isLoading:Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
                vcity = mbunlde.getString("city")!!
                vcategory = mbunlde.getString("category")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
            vcity= ""
            vcategory = ""
        }
    }
}