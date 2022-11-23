package com.example.tugasbesar

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.databinding.ActivityEditBinding
import com.example.tugasbesar.room.Constant
import com.example.tugasbesar.room.User
import com.example.tugasbesar.models.Users
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.*
import org.json.JSONObject
import org.w3c.dom.Text
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var mbunlde : Bundle
    private lateinit var binding: ActivityEditBinding
    private lateinit var usernameEdit : TextInputLayout
    private lateinit var passwordEdit : TextInputLayout
    private lateinit var emailEdit : TextInputLayout
    private lateinit var phoneEdit : TextInputLayout
    private lateinit var tanggalEdit : TextInputLayout
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var passworddb :String
    lateinit var usernamedb :String
    lateinit var emaildb :String
    lateinit var telpdb :String
    lateinit var tgldb :String
    private lateinit var loading : LinearLayout
    private var queue: RequestQueue? = null
    private val calender = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd, MMM, yyyy", Locale.US)
    val db by lazy { UserDB(this) }
    lateinit var userid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = findViewById(R.id.layout_loading)
        usernameEdit = binding.userInput
        passwordEdit = binding.passInput
        emailEdit = binding.emailInput
        phoneEdit = binding.phoneInput
        tanggalEdit = binding.tglInput
        val user : String = usernameEdit.getEditText()?.getText().toString()
        val pass : String = passwordEdit.getEditText()?.getText().toString()
        val email : String = emailEdit.getEditText()?.getText().toString()
        val phone : String = phoneEdit.getEditText()?.getText().toString()
        val tgl : String = tanggalEdit.getEditText()?.getText().toString()
        queue= Volley.newRequestQueue(this)
        getBundle()
//        autofill(vuser ,vpass)
        getAkun(vuser ,vpass)
        tanggalEdit.setStartIconOnClickListener(View.OnClickListener{
            DatePickerDialog(this,this,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
        })
        button_update.setOnClickListener {
            updateUser(user,pass,email,tgl,phone)
//            runBlocking(){
//                val usernameDb = async {
//                    val Account: User? = db.noteDao().getUsername(usernameEdit.getEditText()?.getText().toString())
//                    if (Account != null) {
//                        Account.username
//                    } else {
//                        null
//                    }
//                }
//                usernamedb = usernameDb.await().toString()
//            }
//            if(usernameEdit.getEditText()?.getText().toString() == usernamedb && usernameEdit.getEditText()?.getText().toString()!=vuser){
//                usernameEdit.setError("Username Sudah Ada")
//            }else{
//                CoroutineScope(Dispatchers.IO).launch {
//                    db.noteDao().updateNote(
//                        User(userid.toInt(), usernameEdit.getEditText()?.getText().toString(),
//                            passwordEdit.getEditText()?.getText().toString(),emailEdit.getEditText()?.getText().toString(),
//                            phoneEdit.getEditText()?.getText().toString(), tanggalEdit.getEditText()?.getText().toString())
//                    )
//                    finish()
//                }
                val intent = Intent(this,profile::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",usernameEdit.getEditText()?.getText().toString())
                mBundle.putString("password",passwordEdit.getEditText()?.getText().toString())
                intent.putExtra("profile",mBundle)
                startActivity(intent)
//            }
        }

        button_cancel.setOnClickListener {
            val intent = Intent(this,profile::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",usernameEdit.getEditText()?.getText().toString())
            mBundle.putString("password",passwordEdit.getEditText()?.getText().toString())
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }
    }
    override fun onDateSet(view: DatePicker?, year:Int, month:Int, dayofMonth : Int) {
        Log.e("Calender","$year -- $month -- $dayofMonth")
        calender.set(year, month, dayofMonth)
        displayFormattedDate(calender.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long){
        findViewById<EditText>(R.id.edit_tgl).setText(formatter.format(timestamp))
        Log.i("Formatting",timestamp.toString())
    }

//    private fun autofill(user : String ,pass : String){
//        runBlocking(){
//            var idDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                if (Account != null) {
//                    Account.id
//                } else {
//                    null
//                }
//            }
//            val usernameDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                if (Account != null) {
//                    Account.username
//                } else {
//                    null
//                }
//            }
//            val passwordDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                Log.d("MainActivity","dbResponse: $Account")
//                if (Account != null) {
//                    Account.password
//                } else {
//                    null
//                }
//            }
//            val emailDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                Log.d("MainActivity","dbResponse: $Account")
//                if (Account != null) {
//                    Account.email
//                } else {
//                    null
//                }
//            }
//            val phoneDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                Log.d("MainActivity","dbResponse: $Account")
//                if (Account != null) {
//                    Account.noTelp
//                } else {
//                    null
//                }
//            }
//            val tanggalDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                Log.d("MainActivity","dbResponse: $Account")
//                if (Account != null) {
//                    Account.tanggallahir
//                } else {
//                    null
//                }
//            }
//            userid = idDb.await().toString()
//            usernamedb = usernameDb.await().toString()
//            passworddb = passwordDb.await().toString()
//            emaildb = emailDb.await().toString()
//            telpdb = phoneDb.await().toString()
//            tgldb = tanggalDb.await().toString()
//        }
//    }
//    fun setText(){
//        edit_username.setText(usernamedb)
//        edit_password.setText(passworddb)
//        edit_email.setText(emaildb)
//        edit_phone.setText(telpdb)
//        edit_tgl.setText(tgldb)
//    }
    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
        }

    }

    private fun getAkun(Username:String,Password:String){
        setLoading(true)
        val StringRequest: StringRequest = object
            : StringRequest(
            Method.GET, AkunApi.GET_BY_USERNAME + Username + "/" + Password,
            Response.Listener { response->
                val gson = Gson()

                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val akun = jsonArray.getJSONObject(i)
                    binding.userInput.getEditText()!!.setText(akun.getString("username"))
                    binding.passInput.getEditText()!!.setText(akun.getString("password"))
                    binding.emailInput.getEditText()!!.setText(akun.getString("email"))
                    binding.phoneInput.getEditText()!!.setText(akun.getString("no_telp"))
                    binding.tglInput.getEditText()!!.setText(akun.getString("birth_date"))
                    setLoading(false)
                }
            }, Response.ErrorListener { error->
                setLoading(false)
//                try{
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
//                    usernameInput.setError("Akun belum Terdaftar")
//                    Toast.makeText(
//                        this@MainActivity,
//                        "Akun Belum Terdaftar",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }catch (e: Exception){
//                    Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
//                }
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String,String>()
                headers["Accept"] = "application/json"
                return headers
            }
//
//            override fun getParams(): Map<String, String> {
//                val params = HashMap<String, String>()
//                params["username"] = Username
//                params["password"] = Password
//                return params
//            }
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

    private fun updateUser(username:String,password:String,email:String,
    no_telp:String,tgl_lahir:String){
        val user = Users(username,password,email,no_telp,tgl_lahir)

        val stringRequest: StringRequest =
            object: StringRequest(Method.PUT, AkunApi.UPDATE_URL + username, Response.Listener { response ->
                val jsonObject = JSONObject(response)
                Toast.makeText(this, "Update User Success", Toast.LENGTH_SHORT).show()
                    finish()
            }, Response.ErrorListener { error ->
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }

                @Throws(AuthFailureError::class)
                override fun getBody(): ByteArray {
                    val gson = Gson()
                    val requestBody = gson.toJson(user)
                    return requestBody.toByteArray(StandardCharsets.UTF_8)
                }

                override fun getBodyContentType(): String {
                    return "application/json"
                }
            }

        queue!!.add(stringRequest)
    }

}
