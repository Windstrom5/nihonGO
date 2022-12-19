package com.example.tugasbesar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.databinding.ActivityForgetPasswordBinding
import com.example.tugasbesar.models.Users
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_forget_password.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class ForgetPassword : AppCompatActivity() {
    private val myPreference = "myPref"
    private val userkey = "userKey"
    private val passkey = "passwordKey"
    var sharedPreferences: SharedPreferences? = null
    private lateinit var binding: ActivityForgetPasswordBinding
    private lateinit var loading : LinearLayout
    private lateinit var username : TextInputLayout
    private lateinit var password : TextInputLayout
    private lateinit var confirmpassword : TextInputLayout
    private var queue: RequestQueue? = null
    private lateinit var save : Button
    private lateinit var cancel : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = findViewById(R.id.layout_loading)
        username = binding.userForget
        password = binding.passwordForget
        confirmpassword = binding.confirmForget
        save = binding.buttonSave
        cancel = binding.buttonCancel
        queue= Volley.newRequestQueue(this)
        save.setOnClickListener{
            if(username.getEditText()?.getText().toString().isEmpty()){
                username.setError("Username Tidak Boleh Kosong")
                Toast.makeText(this@ForgetPassword,"Username Tidak Boleh Kosong",Toast.LENGTH_SHORT).show()
            }else if(password.getEditText()?.getText().toString().isEmpty()) {
                password.setError("Password Tidak Boleh Kosong")
                Toast.makeText(this@ForgetPassword, "Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            }else if (confirmpassword.getEditText()?.getText().toString().isEmpty()){
                confirmpassword.setError("Konfirmasi Password Tidak Boleh Kosong")
                Toast.makeText(this@ForgetPassword, "Konfirmasi Password Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            }else if(password.getEditText()?.getText().toString() != confirmpassword.getEditText()?.getText().toString()){
                confirmpassword.setError("Password Tidak Sama")
                Toast.makeText(this@ForgetPassword,"Password Tidak Sama",Toast.LENGTH_SHORT).show()
            }else{
                getAkun(username.getEditText()?.getText().toString(),password.getEditText()?.getText().toString())
            }
        }
    }

    private fun getAkun(Username:String,Password:String){
        setLoading(true)
        val StringRequest: StringRequest = object : StringRequest(Method.GET, AkunApi.GET_BY_USERNAME + Username + "/" + Password + "/" + "forgot",
            Response.Listener { response->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                if(jsonArray.length()==0){
                    username.setError("Akun belum Terdaftar")
                }else{
                    for (i in 0 until jsonArray.length()) {
                        val akun = jsonArray.getJSONObject(i)
                        val username = akun.getString("username")
                        val password = passwordForget.getEditText()?.getText().toString()
                        val email = akun.getString("email")
                        val no_telp = akun.getString("no_telp")
                        val birth_date = akun.getString("birth_date")
                        val profile = akun.getString("photo_profile")
                        val otp = akun.getString("otp_status")
                        verifiedStatus(username,password,email,no_telp,birth_date,profile,otp)
                    }
                }
                setLoading(false)
//                }
            }, Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@ForgetPassword,
                        "Akun Belum Terdaftar",
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@ForgetPassword,e.message, Toast.LENGTH_SHORT).show()
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
                params["username"] = Username
                params["password"] = Password
                return params
            }
        }
        queue!!.add(StringRequest)
    }

    private fun editPassword(username : String,pass : String,email: String,phone:String,tgl:String,profile:String,otp:String){
        setLoading(true)
        val akun = Users(
            username,
            pass,
            email,
            phone,
            tgl,
            profile,
            otp
        )
        Log.d("apa coba",profile)
        val StringRequest:StringRequest = object : StringRequest(Method.PUT,AkunApi.UPDATE_URL + username + "/" +"verifikasi",
            Response.Listener { response ->
                val gson = Gson()
                val akun = gson.fromJson(response, Users::class.java)
                if(akun != null)
                    Toast.makeText(this@ForgetPassword,"Edit Password Successfull", Toast.LENGTH_SHORT).show()
                sharedPreferences = getSharedPreferences(myPreference,
                    Context.MODE_PRIVATE)
                val editor: SharedPreferences.Editor =
                    sharedPreferences!!.edit()
                editor.putString(userkey, username.trim())
                editor.putString(passkey, pass.trim())
                editor.apply()
                val intent = Intent(this,MainActivity::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",username)
                mBundle.putString("password",pass)
                intent.putExtra("profile",mBundle)
                startActivity(intent)
                finish()
                setLoading(false)
            },Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@ForgetPassword,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@ForgetPassword,e.message, Toast.LENGTH_SHORT).show()
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
                params.put("username",username)
                params.put("password",pass)
                params.put("email",email)
                params.put("no_telp",phone)
                params.put("birth_date",tgl)
                params.put("photo_profile",profile)
                params.put("otp_status",otp)
                return params
            }
//            @Throws(AuthFailureError::class)
//            override fun getBody(): ByteArray {
//                val gson = Gson()
//                val requestBody = gson.toJson(akun)
//                return requestBody.toByteArray(StandardCharsets.UTF_8)
//            }
//
//            override fun getBodyContentType(): String {
//                return "application/json"
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

    private fun verifiedStatus(username : String,pass : String,email: String,phone:String,tgl:String,profile:String,otp:String){
        setLoading(true)
        val StringRequest: StringRequest = object
            : StringRequest(
            Method.GET, AkunApi.GET_BY_USERNAME + username + "/" + pass + "/" + "forgot",
            Response.Listener { response->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val akun = jsonArray.getJSONObject(i)
                    val otpDb = akun.getString("otp_status")
                    if(otpDb == "done"){
                        editPassword(username,pass,email,phone,tgl,profile,otp)
                    }else{
                        Toast.makeText(this@ForgetPassword,"Akun Not Been Verified",Toast.LENGTH_SHORT).show()
                    }
                    setLoading(false)
                }
            }, Response.ErrorListener { error->
                setLoading(false)

            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = java.util.HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(StringRequest)
    }
}