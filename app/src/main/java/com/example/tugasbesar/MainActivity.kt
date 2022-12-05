package com.example.tugasbesar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.tugasbesar.databinding.ActivityMainBinding
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import com.example.tugasbesar.models.Users
import com.example.tugasbesar.api.AkunApi
import com.google.gson.Gson
import com.android.volley.toolbox.Volley
import com.android.volley.RequestQueue

class MainActivity : AppCompatActivity() {
    private lateinit var register : TextView
    private lateinit var btnRegister : TextView
    private lateinit var btnGuest : TextView
    private lateinit var btnLogin : Button
    private lateinit var usernameInput : TextInputLayout
    private lateinit var passwordInput : TextInputLayout
    private lateinit var loading : LinearLayout
    private val myPreference = "myPref"
    private val userkey = "userKey"
    private val passkey = "passwordKey"
    var etUsername : TextInputEditText? = null
    var etPassword : TextInputEditText? = null
    private lateinit var usernameView : TextInputEditText
    private lateinit var passwordView : TextInputEditText
    lateinit var mbunlde : Bundle
    private var queue: RequestQueue? = null
    lateinit var vuser : String
    private var usernamedb :String? = null
    lateinit var account :User
    private var passworddb :String? = null
    lateinit var vpassword : String
    private var emaildb :String? = null
    private var phonedb : String? = null
    private var tgldb : String? = null
    val usernameGet: MutableLiveData<String> = MutableLiveData<String>()
    val passwordGet: MutableLiveData<String> = MutableLiveData<String>()
    val emailGet: MutableLiveData<String> = MutableLiveData<String>()
    val phoneGet: MutableLiveData<String> = MutableLiveData<String>()
    val tglGet: MutableLiveData<String> = MutableLiveData<String>()
    val checkGet: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var sharedPreferences: SharedPreferences? = null
    val db by lazy { UserDB(this) }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getSupportActionBar()?.hide();
        setTitle("User Login")
        setRegister()
        usernameInput = binding.userInput
        passwordInput = binding.passInput
        loading = findViewById(R.id.layout_loading)
        queue= Volley.newRequestQueue(this)
        btnLogin = binding.loginButton
        getBundle()
//        setText()
//        setTextOpen()
        etUsername = binding.user
        etPassword = binding.pass
        btnLogin.setOnClickListener(View.OnClickListener{
            var checkLogin = false
            var check = false
            var checkBundle = false
            usernameInput.setError(null)
            passwordInput.setError(null)
            val user : String = usernameInput.getEditText()?.getText().toString()
            val pass : String = passwordInput.getEditText()?.getText().toString()
            if(user.isEmpty()){
                usernameInput.setError("Username tidak boleh Kosong")
                checkLogin = false
                return@OnClickListener
            }else if(pass.isEmpty()){
                passwordInput.setError("Password tidak boleh Kosong")
                checkLogin = false
                return@OnClickListener
            }else if(user.isEmpty()&&pass.isEmpty()){
                usernameInput.setError("Username tidak boleh Kosong")
                passwordInput.setError("Password tidak boleh Kosong")
                checkLogin = false
                return@OnClickListener
            }
            getAkun(user,pass)
//            usernameGet.observe(this, Observer { String->
//                usernamedb = usernameGet.value
//            })
//            passwordGet.observe(this, Observer { String->
//                passworddb = passwordGet.value
//            })
//            checkGet.observe(this, Observer { String->
//                check = checkGet.value.toString().toBoolean()
//            })
//            runBlocking(){
//                val usernameDb = async {
//                    val Account: User? = db.noteDao().getAccount(user, pass)
//                    if (Account != null) {
//                        Account.username
//                    } else {
//                        null
//                    }
//                }
//                val passwordDb = async {
//                    val Account: User? = db.noteDao().getAccount(user, pass)
//                    Log.d("MainActivity","dbResponse: $Account")
//                    if (Account != null) {
//                        Account.password
//                    } else {
//                        null
//                    }
//                }
//                usernamedb = usernameDb.await().toString()
//                passworddb = passwordDb.await().toString()
//            }
//            CoroutineScope(Dispatchers.IO).launch {
//                val Account: User? = db.noteDao().getAccount(user,pass)
//                if(Account!=null){
//                    Log.d("MainActivity","dbResponse: $Account")
//                    withContext(Dispatchers.Main){
//                        usernamedb =  Account.username
//                        passworddb = Account.password
//                    }
//                }else{
//                    usernamedb = ""
//                    passworddb = ""
//                }
//            }
            getAkun(user,pass)
//            if (check == true){
//                checkLogin=true
//            }else{
//                usernameInput.setError("Username Atau Password Salah")
//                return@OnClickListener
//            }
//            if(!checkLogin) {
//                return@OnClickListener
//            }else{
//            }
        })
        btnRegister = findViewById(R.id.registerText)
        btnRegister.setOnClickListener(){
            val intent = Intent(this,RegisterView::class.java)
            startActivity(intent)
        }
        btnGuest = findViewById(R.id.textguest)
        btnGuest.setOnClickListener(){
            val intent = Intent(this,kota::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(a)
    }

    fun setRegister(){
        register = binding.registerText
        register.setTextColor(Color.parseColor("#001eff"))
        btnGuest = binding.textguest
        btnGuest.setTextColor(Color.parseColor("#001eff"))

    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpassword = mbunlde.getString("password")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpassword = ""
        }
    }

    fun setText(){
        usernameView = binding.user
        usernameView.setText(vuser,TextView.BufferType.EDITABLE)
        passwordView = binding.pass
        passwordView.setText(vpassword,TextView.BufferType.EDITABLE)
    }

    fun setTextOpen(){
        usernameView = binding.user
        passwordView = binding.pass
        sharedPreferences = getSharedPreferences(myPreference,
            Context.MODE_PRIVATE)
        if (sharedPreferences!!.contains(userkey)){
            usernameView.setText(sharedPreferences!!.getString(userkey, ""),TextView.BufferType.EDITABLE)
        }
        if (sharedPreferences!!.contains(passkey)) {
            passwordView.setText(sharedPreferences!!.getString(passkey, ""),TextView.BufferType.EDITABLE)
        }
    }

//    fun readData(view: View) {
//        usernameView = binding.user
//        passwordView = binding.pass
//        var strUser: String =
//            etUsername?.text.toString().trim()
//        var strPass: String =
//            etPassword?.text.toString().trim()
//        strUser = sharedPreferences!!.getString(user, "")!!
//        strPass = sharedPreferences!!.getString(pass, "")!!
//        sharedPreferences = getApplicationContext().getSharedPreferences(myPreference,
//            0)
//        if (sharedPreferences!!.contains(user)) {
//            usernameView.setText(strUser,TextView.BufferType.EDITABLE)
//        }
//        if (sharedPreferences!!.contains(pass)) {
//            usernameView.setText(strPass,TextView.BufferType.EDITABLE)
//        }
//        Toast.makeText(baseContext, "Data retrieved",
//            Toast.LENGTH_SHORT).show()
//    }

    fun makeData(user: String,pass:String){
        usernamedb = user
        passworddb = pass
    }

//    fun loadData(usercheck:String,passcheck:String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val Account = db.noteDao().getAccount(usercheck,passcheck)
//            usernamedb = Account.get(0).username
//            passworddb = Account.get(0).password
//        }
//    }

    private fun getAkun(Username:String,Password:String){
        setLoading(true)
        val StringRequest: StringRequest = object : StringRequest(Method.GET,AkunApi.GET_BY_USERNAME + Username + "/" + Password,
            Response.Listener { response->
                val gson = Gson()
                val akun = gson.fromJson(response, Users::class.java)
                Log.d("MainActivity","dbResponse: ${akun.username}")
//                val usernameDatabase = akun.username
//                val passwordDatabase = akun.password
//                val emailDatabase = akun.email
//                val phoneDatabase = akun.no_telp
//                val dateDatabase = akun.birth_date
//                usernameGet.postValue(usernameDatabase)
//                passwordGet.postValue(passwordDatabase)
//                emailGet.postValue(emailDatabase)
//                phoneGet.postValue(phoneDatabase)
//                tglGet.postValue(dateDatabase)
//                checkGet.postValue(true)
////                getData(usernamedb.toString(),passworddb.toString(),emaildb.toString(),
////                    phonedb.toString(),tgldb.toString())
//                Toast.makeText(this,"Data Berhasil Diambil!",Toast.LENGTH_SHORT).show()
//                setLoading(false)
                if(akun!=null){
//                    val userSave: String =
//                        usernameInput.getEditText()?.getText().toString().trim()
//                    val passSave: String =
//                        passwordInput.getEditText()?.getText().toString().trim()
//                    val editor: SharedPreferences.Editor =
//                        sharedPreferences!!.edit()
//                    editor.putString(userkey, userSave)
//                    editor.putString(passkey, passSave)
//                    editor.apply()
                    Toast.makeText(
                        this@MainActivity,
                        "Berhasil Login",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent(this,kota::class.java)
                    val mBundle = Bundle()
                    mBundle.putString("username",usernameInput.getEditText()?.getText().toString())
                    mBundle.putString("password",passwordInput.getEditText()?.getText().toString())
                    intent.putExtra("profile",mBundle)
                    startActivity(intent)
                    finish()

                    setLoading(false)
                }
            }, Response.ErrorListener { error->
                setLoading(false)
                checkGet.postValue(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    usernameInput.setError("Akun belum Terdaftar")
                    Toast.makeText(
                        this@MainActivity,
                        "Akun Belum Terdaftar",
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@MainActivity,e.message,Toast.LENGTH_SHORT).show()
                }
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String,String>()
                headers["Accept"] = "application/json"
                return headers
            }

            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["username"] = usernameInput.getEditText()?.getText().toString()
                params["password"] = passwordInput.getEditText()?.getText().toString()
                return params
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

    fun getData(username : String, password: String, email:String,telp:String,birth_date:String){
        usernamedb = username
        passworddb = password
        emaildb = email
        phonedb = telp
        tgldb = birth_date
//        check = true
    }

    companion object {
        private const val TAG = "MAIN_TAG"
    }
}