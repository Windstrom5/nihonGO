package com.example.tugasbesar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.databinding.ActivityDetailsBinding
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_details.*
import org.json.JSONObject

class DetailsActivity : AppCompatActivity() {
    private lateinit var mbunlde : Bundle
    private lateinit var namaWisata : String
    private var nama: TextView? = null
    private var detail: TextView? = null
    private var lokasi: TextView? = null
    private var hargaTiket: TextView? = null
    private lateinit var loading : LinearLayout
    private var queue: RequestQueue? = null
    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = findViewById(R.id.layout_loading)
        queue= Volley.newRequestQueue(this)

        nama = binding.namaWisata
        detail = binding.detailsWisata
        lokasi = binding.lokasiWisata
        hargaTiket = binding.hargaTiket
//        getData(namaWisata)

        button_ticket.setOnClickListener(){
            val intent = Intent(this,addTiket::class.java)
            startActivity(intent)
        }

        button_location.setOnClickListener(){
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        finish()
    }

    private fun getData(nama: String){
        setLoading(true)
        val StringRequest: StringRequest = object
            : StringRequest(
            Method.GET, tempatWisataApi.GET_BY_NAMA_URL + nama ,
            Response.Listener { response->
                val gson = Gson()

                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val data = jsonArray.getJSONObject(i)
                    binding.namaWisata.setText(data.getString("nama"))
                    binding.detailsWisata.setText(data.getString("detail"))
                    binding.lokasiWisata.setText(data.getString("lokasi"))
                    binding.hargaTiket.setText(data.getString("harga"))
                    setLoading(false)
                }
            }, Response.ErrorListener { error->
                setLoading(false)
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

    fun setLoading(isLoading:Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            loading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            loading!!.visibility = View.INVISIBLE
        }
    }
}