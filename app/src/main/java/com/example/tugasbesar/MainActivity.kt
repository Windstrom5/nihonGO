package com.example.tugasbesar

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.lang.NullPointerException
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var register : TextView
    private lateinit var btnRegister : TextView
    private lateinit var btnGuest : TextView
    private lateinit var btnLogin : Button
    private lateinit var usernameInput : TextInputLayout
    private lateinit var passwordInput : TextInputLayout
    private lateinit var usernameView : TextInputEditText
    lateinit var mbunlde : Bundle
    lateinit var vuser : String
    lateinit var vpassword : String

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
            if (user == vuser&&pass == vpassword){
                checkLogin=true
            }else{
                passwordInput.setError("Password Salah")
                return@OnClickListener
            }

            if(!checkLogin) {
                return@OnClickListener
            }else{
                val intent = Intent(this,home::class.java)
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
            val intent = Intent(this,home::class.java)
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
        }
    }

    fun setText(){
        usernameView = findViewById(R.id.user)
        usernameView.setText(vuser,TextView.BufferType.EDITABLE)
    }
}