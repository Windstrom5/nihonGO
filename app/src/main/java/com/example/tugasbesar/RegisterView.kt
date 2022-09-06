package com.example.tugasbesar

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isEmpty
import androidx.core.view.isNotEmpty
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class RegisterView : AppCompatActivity() {
    private lateinit var register : TextView
    private lateinit var btnRegister : Button
    private lateinit var btnlogin : TextView
    private lateinit var userRegister : TextInputLayout
    private lateinit var passRegister : TextInputLayout
    private lateinit var confirmRegister : TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_view)
        getSupportActionBar()?.hide();
        setTitle("Register Page")
        setRegister()
        btnRegister = findViewById(R.id.regisButton)
        btnlogin = findViewById(R.id.loginText)
        userRegister = findViewById(R.id.userRegis)
        passRegister =  findViewById(R.id.passwordRegis)
        confirmRegister = findViewById(R.id.confirmRegis)
        btnRegister.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val pass : String = passRegister.getEditText()?.getText().toString()
            val confirmpass : String = confirmRegister.getEditText()?.getText().toString()
            val user : String = userRegister.getEditText()?.getText().toString()
            if(user.isEmpty()){
                userRegister.setError("Username tidak boleh Kosong")
                checkLogin = false
            }else if(pass.isEmpty()){
                passRegister.setError("Password tidak boleh Kosong")
                checkLogin = false
            }else if(confirmpass.isEmpty()){
                confirmRegister.setError("Password Tidak Sama")
                checkLogin = false
            }else if(user.isEmpty()&&pass.isEmpty()&&confirmpass.isEmpty()){
                userRegister.setError("Username tidak boleh Kosong")
                passRegister.setError("Password tidak boleh Kosong")
                confirmRegister.setError("Password Tidak Sama")
                checkLogin = false
            }
            if (pass == confirmpass && user.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty()){
                checkLogin=true
            }else if(pass != confirmpass){
                confirmRegister.setError("Password Tidak Sama")
            }
            if(!checkLogin) {
                return@OnClickListener
            }else{
                val intent = Intent(this,MainActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",userRegister.getEditText()?.getText().toString())
                mBundle.putString("password",passRegister.getEditText()?.getText().toString())
                intent.putExtra("register",mBundle)
                startActivity(intent)
            }
        })

        btnlogin.setOnClickListener(){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
    fun setRegister(){
        register = findViewById(R.id.loginText)
        register.setTextColor(Color.parseColor("#001eff"))
    }
}