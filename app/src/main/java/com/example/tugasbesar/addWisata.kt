package com.example.tugasbesar

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isEmpty
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.api.EventApi
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.entity.itemList
import com.example.tugasbesar.models.Event
import com.example.tugasbesar.models.TempatWisata
import com.example.tugasbesar.models.Users
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_add_tempat_wisata.*
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.nio.charset.StandardCharsets
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap


class addWisata : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    private var etNama: EditText? = null
    private var etPrice: EditText? =null
    private var etAlamat: EditText? = null
    private var etRating: EditText? = null
    private var etLat: EditText? = null
    private var etLong: EditText? = null
    private var etCity: EditText? = null
    private lateinit var mbunlde : Bundle
    private lateinit var vuser : String
    private lateinit var vpass : String
    private lateinit var vlokasi : String
    private var layout_loading: LinearLayout? = null
    private var edCategory:AutoCompleteTextView? = null
    private var queue: RequestQueue? = null
    private lateinit var ratingLayout : TextInputLayout
    private lateinit var categoryLayout: TextInputLayout
    private lateinit var namaLayout : TextInputLayout
    private lateinit var alamatLayout: TextInputLayout
    private lateinit var latitudeLayout : TextInputLayout
    private lateinit var longtitudeLayout : TextInputLayout
    private lateinit var priceLayout : TextInputLayout
    private lateinit var layoutCity : TextInputLayout
    private val calender = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd, MMM, yyyy",Locale.US)
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
        getBundle()
        etCity = findViewById(R.id.et_lokasi)
        queue= Volley.newRequestQueue(this)
        etNama = findViewById(R.id.et_nama)
        etPrice = findViewById(R.id.et_harga)
        etAlamat = findViewById(R.id.et_alamat)
        etRating = findViewById(R.id.et_rating)
        etLat = findViewById(R.id.et_lat)
        etLong = findViewById(R.id.et_long)
        layout_loading = findViewById(R.id.layout_loading)
//        categoryLayout = findViewById(R.id.jenis_wisata)
        edCategory = findViewById(R.id.ed_jenis)
        latitudeLayout = findViewById(R.id.layout_lat)
        categoryLayout = findViewById(R.id.jenis_wisata)
        alamatLayout = findViewById(R.id.layout_alamat)
        priceLayout = findViewById(R.id.layout_harga)
        longtitudeLayout = findViewById(R.id.layout_long)
        ratingLayout = findViewById(R.id.layout_rating)
        namaLayout = findViewById(R.id.layout_nama)
        layoutCity = findViewById(R.id.layout_lokasi)
        etCity!!.setText(vlokasi)
        etCity!!.setFocusable(false)
        setExposeDropDownMenu()
        edCategory!!.setOnItemClickListener{adapterView, view, position, id ->
            if (edCategory!!.text.toString() == "Event") {
                namaLayout.setHint("Nama Event")
                ratingLayout.setHint("Tanggal")
                ratingLayout.setStartIconDrawable(R.drawable.ic_baseline_calendar_month_24)
                ratingLayout.setStartIconOnClickListener(View.OnClickListener{
                    DatePickerDialog(this,this,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
                })
                etRating!!.setFocusable(false)
                latitudeLayout.visibility = View.VISIBLE
                longtitudeLayout.visibility = View.VISIBLE
            }else if(edCategory!!.text.toString() == "Tempat Wisata"){
                namaLayout.setHint("Nama Tempat Wisata")
                ratingLayout.setHint("Rating Tempat Wisata")
                ratingLayout.setStartIconDrawable(R.drawable.ic_star)
                ratingLayout.setStartIconOnClickListener(null);
                etRating!!.setFocusable(true)
                latitudeLayout.visibility = View.VISIBLE
                longtitudeLayout.visibility = View.VISIBLE
            }else if(edCategory!!.text.toString() == "Akomodasi"){
                namaLayout.setHint("Nama Tempat Akomodasi")
                ratingLayout.setHint("Rating Tempat Akomodasi")
                ratingLayout.setStartIconOnClickListener(null);
                etRating!!.setFocusable(true)
                ratingLayout.setStartIconDrawable(R.drawable.ic_star)
            }else{
                namaLayout.setHint("Nama Tempat Kuliner")
                ratingLayout.setHint("Rating Tempat Kuliner")
                ratingLayout.setStartIconOnClickListener(null);
                etRating!!.setFocusable(true)
                ratingLayout.setStartIconDrawable(R.drawable.ic_star)
            }
        }
        var current = ""

        etPrice!!.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val stringText = s.toString()

                if(stringText != current) {
                    etPrice!!.removeTextChangedListener(this)

                    val locale: Locale = Locale.US
                    val currency = Currency.getInstance(locale)
                    val cleanString = stringText.replace("[${currency.symbol},.]".toRegex(), "")
                    val parsed = cleanString.toDouble()
                    val formatted = NumberFormat.getCurrencyInstance(locale).format(parsed / 100)

                    current = formatted
                    etPrice!!.setText(formatted)
                    etPrice!!.setSelection(formatted.length)
                    etPrice!!.addTextChangedListener(this)
                }
            }
        })

        var checkInputan = false

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener{ finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        btnSave.setOnClickListener(){
            namaLayout.setError(null)
            categoryLayout.setError(null)
            alamatLayout.setError(null)
            ratingLayout.setError(null)
            priceLayout.setError(null)
            latitudeLayout.setError(null)
            longtitudeLayout.setError(null)
            if(etNama!!.getText().isEmpty()){
                namaLayout.setError("Nama Tidak Boleh Kosong!")
                checkInputan = false
                Toast.makeText(this,"Nama Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
            }else if(edCategory!!.getText().isEmpty()){
                categoryLayout.setError("Category Tidak Boleh Kosong!")
                checkInputan = false
                Toast.makeText(this,"Category Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
            }else if(etAlamat!!.getText().isEmpty()){
                alamatLayout.setError("Alamat Tidak Boleh Kosong!")
                checkInputan = false
            }else if(etRating!!.getText().isEmpty()){
                if(edCategory!!.getText().toString() == "Event"){
                    ratingLayout.setError("Tanggal Event Tidak Boleh Kosong!")
                    Toast.makeText(this,"Tanggal Event Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
                }else{
                    ratingLayout.setError("Rating Tempat Tidak Boleh Kosong!")
                    Toast.makeText(this,"Rating Tempat Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
                }
                checkInputan = false
            }else if(etPrice!!.getText().isEmpty()){
                priceLayout.setError("Price Tidak Boleh Kosong!")
                checkInputan = false
                Toast.makeText(this,"Price Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
            }else if(etLat!!.getText().isEmpty()){
                latitudeLayout.setError("Latitude Tidak Boleh Kosong!")
                checkInputan = false
                Toast.makeText(this,"Latitude Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
            }else if(etLong!!.getText().isEmpty()){
                longtitudeLayout.setError("Longtitude Tidak Boleh Kosong!")
                checkInputan = false
                Toast.makeText(this,"Longtitude Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
            }else{
                checkInputan = true
            }
            if(!checkInputan){
                return@setOnClickListener
            }else{
                AddTempat()
            }
        }
    }

    fun setExposeDropDownMenu() {
        val adapterCategory: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_list, Category_LIST)
        edCategory!!.setAdapter(adapterCategory)
    }


    private fun AddTempat(){
        setLoading(true)
        if(edCategory!!.text.toString() == "Tempat Wisata"){
            val tempatWisata = TempatWisata(
                etNama!!.getText().toString(),
                etAlamat!!.getText().toString(),
                etRating!!.getText().toString(),
                etPrice!!.getText().toString(),
                etCity!!.getText().toString(),
                etLat!!.getText().toString(),
                etLong!!.getText().toString()
            )
            val StringRequest:StringRequest = object : StringRequest(Method.POST, tempatWisataApi.ADD_URL,
                Response.Listener { response ->
                    val gson = Gson()
                    val tempatWisata = gson.fromJson(response, TempatWisata::class.java)
                    if(tempatWisata != null)
                        Toast.makeText(this@addWisata,"Tempat Wisata Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
//                    val returnIntent = Intent()
//                    setResult(RESULT_OK, returnIntent)
                    MotionToast.Companion.darkToast(this,
                        "Added Complete ",
                        "Added "+etNama!!.getText().toString()+" Complete",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
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
                    params.put("price",etPrice!!.getText().toString())
                    params.put("city",etCity!!.getText().toString())
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
                etRating!!.getText().toString(),
                etPrice!!.getText().toString(),
                etCity!!.getText().toString(),
                etLat!!.getText().toString().toDouble(),
                etLong!!.getText().toString().toDouble()
            )
            val StringRequest:StringRequest = object : StringRequest(Method.POST, EventApi.ADD_URL,
                Response.Listener { response ->
                    val gson = Gson()
                    val event = gson.fromJson(response, TempatWisata::class.java)
                    if(event != null)
                        Toast.makeText(this@addWisata,"Tempat Wisata Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
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
                    params.put("price",etPrice!!.getText().toString())
                    params.put("city",etCity!!.getText().toString())
                    params.put("lat",etLat!!.getText().toString())
                    params.put("longi",etLong!!.getText().toString())
                    return params
                }
            }
            queue!!.add(StringRequest)
        }
    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
                vlokasi = mbunlde.getString("city")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
            vlokasi = ""
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

    override fun onDateSet(view: DatePicker?, year:Int, month:Int , dayofMonth : Int) {
        Log.e("Calender","$year -- $month -- $dayofMonth")
        calender.set(year, month, dayofMonth)
        displayFormattedDate(calender.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long){
        findViewById<EditText>(R.id.et_rating).setText(formatter.format(timestamp))
        Log.i("Formatting",timestamp.toString())
    }

    override fun onBackPressed() {
        finish()
    }
}