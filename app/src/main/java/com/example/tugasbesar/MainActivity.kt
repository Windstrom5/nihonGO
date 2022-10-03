package com.example.tugasbesar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.tugasbesar.databinding.ActivityMainBinding
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {
    private lateinit var register : TextView
    private lateinit var btnRegister : TextView
    private lateinit var btnGuest : TextView
    private lateinit var btnLogin : Button
    private lateinit var usernameInput : TextInputLayout
    private lateinit var passwordInput : TextInputLayout
    private val myPreference = "myPref"
    private val userkey = "userKey"
    private val passkey = "passwordKey"
    var etUsername : TextInputEditText? = null
    var etPassword : TextInputEditText? = null
    private lateinit var usernameView : TextInputEditText
    private lateinit var passwordView : TextInputEditText
    lateinit var mbunlde : Bundle
    lateinit var vuser : String
    lateinit var usernamedb :String
    lateinit var account :User
    lateinit var passworddb :String
    lateinit var vpassword : String
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
        btnLogin = binding.loginButton
        getBundle()
        setText()
        setTextOpen()
        etUsername = binding.user
        etPassword = binding.pass
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
            runBlocking(){
                val usernameDb = async {
                    val Account: User? = db.noteDao().getAccount(user, pass)
                    if (Account != null) {
                        Account.username
                    } else {
                        null
                    }
                }
                val passwordDb = async {
                    val Account: User? = db.noteDao().getAccount(user, pass)
                    Log.d("MainActivity","dbResponse: $Account")
                    if (Account != null) {
                        Account.password
                    } else {
                        null
                    }
                }
                usernamedb = usernameDb.await().toString()
                passworddb = passwordDb.await().toString()
            }
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
            if (user == usernamedb&&pass == passworddb){
                checkLogin=true
            }else{
                usernameInput.setError("Username Atau Password Salah")
                return@OnClickListener
            }
            if(!checkLogin) {
                return@OnClickListener
            }else{
                val userSave: String =
                    usernameInput.getEditText()?.getText().toString().trim()
                val passSave: String =
                    passwordInput.getEditText()?.getText().toString().trim()
                val editor: SharedPreferences.Editor =
                    sharedPreferences!!.edit()
                editor.putString(userkey, userSave)
                editor.putString(passkey, passSave)
                editor.apply()
                val intent = Intent(this,kota::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",usernameInput.getEditText()?.getText().toString())
                mBundle.putString("password",passwordInput.getEditText()?.getText().toString())
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
}