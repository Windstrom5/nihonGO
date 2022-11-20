package com.example.tugasbesar

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.models.TempatWisata
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class addTempatWisata : AppCompatActivity() {
    private var etNama: EditText? = null
    private var etAlamat: EditText? = null
    private var etRating: EditText? = null
    private var etLat: EditText? = null
    private var etLong: EditText? = null
    private var layout_loading: LinearLayout? = null
    private var queue: RequestQueue? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tempat_wisata)

        queue= Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etAlamat = findViewById(R.id.et_alamat)
        etRating = findViewById(R.id.et_rating)
        etLat = findViewById(R.id.et_lat)
        etLong = findViewById(R.id.et_long)
        layout_loading = findViewById(R.id.layout_loading)

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{ finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getLongExtra("id",-1)
        if(id == -1L){
            tvTitle.setText("Tambah Tempat")
            btnSave.setOnClickListener{ createTempat() }
        }else{
            tvTitle.setText("Edit Tempat")
            btnSave.setOnClickListener{ updateTempat(id) }
        }
    }

    private fun createTempat(){
        setLoading(true)

        val tempatWisata = TempatWisata(
            etNama!!.text.toString(),
            etAlamat!!.text.toString(),
            etRating!!.text.toString().toDouble(),
            etLat!!.text.toString().toDouble(),
            etLong!!.text.toString().toDouble()
        )

        val StringRequest: StringRequest = object : StringRequest(Method.POST,tempatWisataApi.ADD_URL,
            Response.Listener { response ->
                val gson = Gson()
                val tempatWisata = gson.fromJson(response, TempatWisata::class.java)

                if(tempatWisata != null)
                    Toast.makeText(this@addTempatWisata,"Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@addTempatWisata,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@addTempatWisata,e.message, Toast.LENGTH_SHORT).show()
                }
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String,String>()
                headers["Accept"] = "application/json"
                return headers
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(tempatWisata)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(StringRequest)
    }

    private fun updateTempat(id: Long){
        setLoading(true)

        val tempatWisata = TempatWisata(
            etNama!!.text.toString(),
            etAlamat!!.text.toString(),
            etRating!!.text.toString().toDouble(),
            etLat!!.text.toString().toDouble(),
            etLong!!.text.toString().toDouble()
        )
        val StringRequest: StringRequest = object : StringRequest(Method.PUT,tempatWisataApi.UPDATE_URL + id,
            Response.Listener { response ->
                val gson = Gson()
                val tempatWisata = gson.fromJson(response, TempatWisata::class.java)

                if(tempatWisata != null)
                    Toast.makeText(this@addTempatWisata,"Data Berhasil Diupdate", Toast.LENGTH_SHORT).show()

                val returnIntent = Intent()
                setResult(RESULT_OK, returnIntent)
                finish()

                setLoading(false)
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@addTempatWisata,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@addTempatWisata,e.message, Toast.LENGTH_SHORT).show()
                }
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String,String>()
                headers["Accept"] = "application/json"
                return headers
            }
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(tempatWisata)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
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
            layout_loading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layout_loading!!.visibility = View.INVISIBLE
        }
    }
}