package com.example.tugasbesar

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.api.EventApi
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.models.Event
import com.example.tugasbesar.models.TempatWisata
import com.example.tugasbesar.models.Users
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_tempat_wisata.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class addWisata : AppCompatActivity() {
    private var etNama: EditText? = null
    private var etAlamat: EditText? = null
    private var etRating: EditText? = null
    private var etLat: EditText? = null
    private var etLong: EditText? = null
    private var layout_loading: LinearLayout? = null
    private var edCategory:AutoCompleteTextView? = null
    private var queue: RequestQueue? = null
    companion object{
        private val Category_LIST = arrayOf(
            "Tempat Wisata",
            "Akomodasi",
            "Kuliner",
            "Event"
        )
    }

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
        edCategory = findViewById(R.id.ed_jenis)
        setExposeDropDownMenu()
        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{ finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        btnSave.setOnClickListener(){
            AddTempat()
        }
    }

    fun setExposeDropDownMenu() {
        val adapterCategory: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_list, Category_LIST)
        ed_jenis!!.setAdapter(adapterCategory)
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
                    Toast.makeText(this@addWisata,"Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()

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
                        this@addWisata,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@addWisata,e.message, Toast.LENGTH_SHORT).show()
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

    private fun AddTempat(){
        setLoading(true)
        if(edCategory!!.text.toString() == "Tempat Wisata"){
            val tempatWisata = TempatWisata(
                etNama!!.getText().toString(),
                etAlamat!!.getText().toString(),
                etRating!!.getText().toString().toDouble(),
                etLat!!.getText().toString().toDouble(),
                etLong!!.getText().toString().toDouble()
            )
            val StringRequest:StringRequest = object : StringRequest(Method.POST, tempatWisataApi.ADD_URL,
                Response.Listener { response ->
                    val gson = Gson()
                    val tempatWisata = gson.fromJson(response, TempatWisata::class.java)
                    if(tempatWisata != null)
                        Toast.makeText(this@addWisata,"tempat Wisata Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                    setLoading(false)
                },Response.ErrorListener { error->
                    setLoading(false)
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@addWisata,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(this@addWisata,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = java.util.HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                override fun getParams(): Map<String, String>? {
                    val params = java.util.HashMap<String, String>()
                    params.put("nama",etNama!!.getText().toString())
                    params.put("alamat",etAlamat!!.getText().toString())
                    params.put("rating",etRating!!.getText().toString())
                    params.put("lat",etLat!!.getText().toString())
                    params.put("longi",etLong!!.getText().toString())
                    return params
                }
            }
            queue!!.add(StringRequest)
        }else if (edCategory!!.text.toString() == "Event"){
            val event = Event(
                etNama!!.getText().toString(),
                etAlamat!!.getText().toString(),
                etRating!!.getText().toString()
            )
            val StringRequest:StringRequest = object : StringRequest(Method.POST, EventApi.ADD_URL,
                Response.Listener { response ->
                    val gson = Gson()
                    val event = gson.fromJson(response, TempatWisata::class.java)
                    if(event != null)
                        Toast.makeText(this@addWisata,"tempat Wisata Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                    setLoading(false)
                },Response.ErrorListener { error->
                    setLoading(false)
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@addWisata,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(this@addWisata,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = java.util.HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                override fun getParams(): Map<String, String>? {
                    val params = java.util.HashMap<String, String>()
                    params.put("namaEvent",etNama!!.getText().toString())
                    params.put("alamat",etAlamat!!.getText().toString())
                    params.put("tgl",etRating!!.getText().toString())
                    return params
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
            layout_loading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layout_loading!!.visibility = View.INVISIBLE
        }
    }
}