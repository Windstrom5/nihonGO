package com.example.tugasbesar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_register_view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var register : TextView
    private lateinit var btnRegister : TextView
    private lateinit var btnGuest : TextView
    private lateinit var btnLogin : Button
    private lateinit var usernameInput : TextInputLayout
    private lateinit var passwordInput : TextInputLayout
    private val myPreference = "myPref"
    private val user = "userKey"
    private val pass = "passwordKey"
    var etUsername : TextInputEditText? = null
    var etPassword : TextInputEditText? = null
    private lateinit var usernameView : TextInputEditText
    private lateinit var passwordView : TextInputEditText
    lateinit var mbunlde : Bundle
    lateinit var vuser : String
    private var usernamedb :String = ""
    private var passworddb :String = ""
    lateinit var vpassword : String
    var sharedPreferences: SharedPreferences? = null
    val db by lazy { UserDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getSupportActionBar()?.hide();
        setTitle("User Login")
        setRegister()
        usernameInput = findViewById(R.id.userInput)
        passwordInput = findViewById(R.id.passInput)
        btnLogin = findViewById<Button>(R.id.loginButton)
        getBundle()
        setText()
        etUsername = findViewById(R.id.user)
        etPassword = findViewById(R.id.pass)
        sharedPreferences = getSharedPreferences(myPreference,
            Context.MODE_PRIVATE)
        if (sharedPreferences!!.contains(user)) {
            etUsername?.setText(sharedPreferences!!.getString(user, ""))
        }
        if (sharedPreferences!!.contains(pass)) {
            etPassword?.setText(sharedPreferences!!.getString(pass, ""))
        }
        btnLogin.setOnClickListener(View.OnClickListener{
            var checkLogin = false
            var checkBundle = false
            val user : String = usernameInput.getEditText()?.getText().toString()
            val pass : String = passwordInput.getEditText()?.getText().toString()
            if(user.isEmpty()){
                usernameInput.setError("Username tidak boleh Kosong")
                checkLogin = false
                return@OnClickListener
            }else if(pass.isEmpty()){
                passwordInput.setError("Username tidak boleh Kosong")
                checkLogin = false
                return@OnClickListener
            }else if(user.isEmpty()&&pass.isEmpty()){
                usernameInput.setError("Username tidak boleh Kosong")
                passwordInput.setError("Username tidak boleh Kosong")
                checkLogin = false
                return@OnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                val Account: User? = db.noteDao().getAccount(user,pass)
                if(Account!=null){
                    Log.d("MainActivity","dbResponse: $Account")
                    withContext(Dispatchers.Main){
                        usernamedb =  Account.username
                        passworddb = Account.password
                    }
                }else{
                    usernamedb = ""
                    passworddb = ""
                }
            }
            if(usernamedb == "" && passworddb == ""){
                usernameInput.setError("Akun Belum Terdaftar di Database")
                return@OnClickListener
            }else if (user == usernamedb&&pass == passworddb){
                checkLogin=true
            }else{
                passwordInput.setError("Password Salah")
                return@OnClickListener
            }
            if(!checkLogin) {
                return@OnClickListener
            }else{
                val intent = Intent(this,kota::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",usernameInput.getEditText()?.getText().toString())
                intent.putExtra("login",mBundle)
                startActivity(intent)
            }
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

    fun setRegister(){
        register = findViewById(R.id.registerText)
        register.setTextColor(Color.parseColor("#001eff"))
        btnGuest = findViewById(R.id.textguest)
        btnGuest.setTextColor(Color.parseColor("#001eff"))

    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("register")!!
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
        usernameView = findViewById(R.id.user)
        usernameView.setText(vuser,TextView.BufferType.EDITABLE)
        passwordView = findViewById(R.id.pass)
        passwordView.setText(vpassword,TextView.BufferType.EDITABLE)
    }

    fun readData(view: View) {
        usernameView = findViewById(R.id.user)
        passwordView = findViewById(R.id.pass)
        var strUser: String =
            etUsername?.text.toString().trim()
        var strPass: String =
            etPassword?.text.toString().trim()
        strUser = sharedPreferences!!.getString(user, "")!!
        strPass = sharedPreferences!!.getString(pass, "")!!
        sharedPreferences = getSharedPreferences(myPreference,
            Context.MODE_PRIVATE)
        if (sharedPreferences!!.contains(user)) {
            usernameView.setText(strUser,TextView.BufferType.EDITABLE)
        }
        if (sharedPreferences!!.contains(pass)) {
            usernameView.setText(strPass,TextView.BufferType.EDITABLE)
        }
        Toast.makeText(baseContext, "Data retrieved",
            Toast.LENGTH_SHORT).show()
    }

//    fun loadData(usercheck:String,passcheck:String) {
//        CoroutineScope(Dispatchers.IO).launch {
//            val Account = db.noteDao().getAccount(usercheck,passcheck)
//            usernamedb = Account.get(0).username
//            passworddb = Account.get(0).password
//        }
//    }
}