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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.api.EventApi
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.databinding.ActivityEditItemBinding
import com.example.tugasbesar.models.Event
import com.example.tugasbesar.models.TempatWisata
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


class editItem : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
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
    private lateinit var vcity : String
    private lateinit var vakun : String
    private lateinit var valamat : String
    private lateinit var vnama : String
    private lateinit var vprice : String
    private lateinit var vrating : String
    private lateinit var vlatitude :String
    private lateinit var vlongtitude : String
    private lateinit var vcategory : String
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
    private lateinit var binding: ActivityEditItemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        getItem(vnama)
//        categoryLayout = findViewById(R.id.jenis_wisata)
        edCategory = findViewById(R.id.ed_jenis)
        latitudeLayout = findViewById(R.id.layout_lat)
        categoryLayout = findViewById(R.id.jenis_wisata)
        categoryLayout.getEditText()!!.setText(vcategory)!!
        alamatLayout = findViewById(R.id.layout_alamat)
        priceLayout = findViewById(R.id.layout_harga)
        longtitudeLayout = findViewById(R.id.layout_long)
        ratingLayout = findViewById(R.id.layout_rating)
        namaLayout = findViewById(R.id.layout_nama)
        layoutCity = findViewById(R.id.layout_lokasi)
        etCity!!.setText(vcity)
        etCity!!.setFocusable(false)
        setExposeDropDownMenu()
        checkDropDown()
        edCategory!!.setOnItemClickListener{adapterView, view, position, id ->
            checkDropDown()
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
                longtitudeLayout.setError("Longitude Tidak Boleh Kosong!")
                checkInputan = false
                Toast.makeText(this,"Longitude Tidak Boleh Kosong!",Toast.LENGTH_SHORT).show()
            }else{
                checkInputan = true
            }
            if(!checkInputan){
                return@setOnClickListener
            }else{
                EditTempat(vnama)
            }
        }
    }

    fun checkDropDown(){
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

    fun setExposeDropDownMenu() {
        val adapterCategory: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.item_list2,Category_LIST)
        edCategory!!.setAdapter(adapterCategory)
    }

    private fun EditTempat(nama: String){
        setLoading(true)
        if(edCategory!!.text.toString() == "Tempat Wisata"){
            val tempatWisata = TempatWisata(
                etNama!!.getText().toString(),
                vuser,
                etAlamat!!.getText().toString(),
                etRating!!.getText().toString(),
                etPrice!!.getText().toString(),
                etCity!!.getText().toString(),
                etLat!!.getText().toString(),
                etLong!!.getText().toString()
            )
            val StringRequest:StringRequest = object : StringRequest(Method.PUT, tempatWisataApi.UPDATE_URL + nama,
                Response.Listener { response ->
                    val gson = Gson()
                    val tempatWisata = gson.fromJson(response, TempatWisata::class.java)
                    if(tempatWisata != null)
                        Toast.makeText(this@editItem,"Tempat Wisata Berhasil Diedit", Toast.LENGTH_SHORT).show()
                    finish()
                    setLoading(false)
                },Response.ErrorListener { error->
                    setLoading(false)
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@editItem,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(this@editItem,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                override fun getParams(): Map<String, String>? {
                    val params = HashMap<String, String>()
                    params.put("name",etNama!!.getText().toString())
                    params.put("user",vuser)
                    params.put("alamat",etAlamat!!.getText().toString())
                    params.put("rating",etRating!!.getText().toString())
                    params.put("price",etPrice!!.getText().toString())
                    params.put("city",etCity!!.getText().toString())
                    params.put("latitude",etLat!!.getText().toString())
                    params.put("longtitude",etLong!!.getText().toString())
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
                        Toast.makeText(this@editItem,"Tempat Wisata Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    val intent = Intent(this,itemActivity::class.java)
                    val mBundle = Bundle()
                    mBundle.putString("username",vuser)
                    mBundle.putString("password",vpass)
                    mBundle.putString("city", vcity)
                    mBundle.putString("category",vcategory)
                    intent.putExtra("profile",mBundle)
                    startActivity(intent)
                    setLoading(false)
                },Response.ErrorListener { error->
                    setLoading(false)
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@editItem,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(this@editItem,e.message, Toast.LENGTH_SHORT).show()
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
                    params.put("latitude",etLat!!.getText().toString())
                    params.put("longtitude",etLong!!.getText().toString())
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
                vcity = mbunlde.getString("city")!!
                vnama = mbunlde.getString("nama")!!
                vakun = mbunlde.getString("user")!!
                valamat = mbunlde.getString("alamat")!!
                vrating = mbunlde.getString("rating")!!
                vprice = mbunlde.getString("price")!!
                vcategory = mbunlde.getString("category")!!
                vlatitude = mbunlde.getString("latitude")!!
                vlongtitude = mbunlde.getString("longtitude")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
            vcity = ""
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
        val intent = Intent(this,itemActivity::class.java)
        val mBundle = Bundle()
        mBundle.putString("username",vuser)
        mBundle.putString("password",vpass)
        mBundle.putString("city", vcity)
        mBundle.putString("category",vcategory)
        intent.putExtra("profile",mBundle)
        startActivity(intent)
    }

    private fun getItem(Name:String){
        setLoading(true)
        val StringRequest: StringRequest = object
            : StringRequest(
            Method.GET, tempatWisataApi.GET_BY_NAMA_URL + Name + "/" + "item",
            Response.Listener { response->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val tempat= jsonArray.getJSONObject(i)
                    binding.layoutNama.getEditText()!!.setText(tempat.getString("name"))
                    binding.layoutAlamat.getEditText()!!.setText(tempat.getString("alamat"))
                    binding.layoutHarga.getEditText()!!.setText(tempat.getString("price"))
                    binding.layoutRating.getEditText()!!.setText(tempat.getString("rating"))
                    binding.layoutLokasi.getEditText()!!.setText(tempat.getString("city"))
                    binding.layoutLat.getEditText()!!.setText(tempat.getString("latitude"))
                    binding.layoutLong.getEditText()!!.setText(tempat.getString("longtitude"))
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
}