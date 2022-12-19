package com.example.tugasbesar

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.chaos.view.PinView
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.databinding.ActivityVerificationBinding
import com.example.tugasbesar.models.Users
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_verification.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.HashMap

class verificationActivity : AppCompatActivity() {
    private lateinit var pinView : PinView
    private lateinit var otpDb: String
    private lateinit var otpInput:String
    private lateinit var loading : LinearLayout
    private lateinit var vuser : String
    private lateinit var vpassword : String
    private lateinit var vemail : String
    private lateinit var vphone: String
    private lateinit var vtgl : String
    private lateinit var vprofile : String
    private lateinit var votp : String
    var sharedPreferences: SharedPreferences? = null
    private val myPreference = "myPref"
    private val userkey = "userKey"
    private val passkey = "passwordKey"
    private lateinit var mbunlde : Bundle
    private var queue: RequestQueue? = null
    private lateinit var binding:ActivityVerificationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()
        pinView = binding.pinview
        loading = findViewById(R.id.layout_loading)
        queue= Volley.newRequestQueue(this)
        pinview.requestFocus()
        val inputMethodManager:InputMethodManager=getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,InputMethodManager.HIDE_IMPLICIT_ONLY)
        getAkun(vuser,vpassword)
        pinView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(char: CharSequence, p1: Int, p2: Int, p3: Int) {
                Toast.makeText(this@verificationActivity,"Please Fill The Coloumn",Toast.LENGTH_SHORT).show()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

            override fun onTextChanged(char: CharSequence, p1: Int, p2: Int, p3: Int) {
                if(char.toString().length == 4){
                    Log.d(vuser,vpassword)
                    getAkun(vuser,vpassword)
                    otpInput = char.toString()
                    if(otpInput != otpDb){
                        Toast.makeText(this@verificationActivity,"WRONG OTP. PLS TRY AGAIN",Toast.LENGTH_SHORT).show()
                    }else{
                        verifiedAccount(vuser,vpassword,vemail,vphone,vtgl,vprofile)
                    }
                }
            }
        })
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

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpassword = mbunlde.getString("password")!!
                vemail = mbunlde.getString("email")!!
                vphone= mbunlde.getString("notelp")!!
                vtgl = mbunlde.getString("tanggallahir")!!
                vprofile =mbunlde.getString("profilepic")!!
                votp =mbunlde.getString("otp")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpassword = ""
        }
    }

    private fun verifiedAccount(username : String,pass : String,email: String,phone:String,tgl:String,profile:String){
        setLoading(true)
        val akun = Users(
            username,
            pass,
            email,
            phone,
            tgl,
            profile,
            "verified"
        )
        Log.d("apa coba",profile)
        val StringRequest:StringRequest = object : StringRequest(Method.PUT,AkunApi.UPDATE_URL + username + "/" +"verifikasi",
            Response.Listener { response ->
                val gson = Gson()
                val akun = gson.fromJson(response, Users::class.java)
                if(akun != null)
                    Toast.makeText(this@verificationActivity,"Account Verification Successfull", Toast.LENGTH_SHORT).show()
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
//                val returnIntent = Intent()
//                setResult(RESULT_OK, returnIntent)
                finish()
                setLoading(false)
            },Response.ErrorListener { error->
                setLoading(false)
                try{
                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                    val errors = JSONObject(responseBody)
                    Toast.makeText(
                        this@verificationActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@verificationActivity,e.message, Toast.LENGTH_SHORT).show()
                }
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String,String>()
                headers["Accept"] = "application/json"
                return headers
            }

            override fun getParams(): Map<String, String>? {
                val params = HashMap<String,String>()
                params.put("username",username)
                params.put("password",pass)
                params.put("email",email)
                params.put("no_telp",phone)
                params.put("birth_date",tgl)
                params.put("photo_profile",profile)
                params.put("otp_status","done")
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

    private fun getAkun(Username:String,Password:String){
        setLoading(true)
        val StringRequest: StringRequest = object
            : StringRequest(
            Method.GET, AkunApi.GET_BY_USERNAME + Username + "/" + Password + "/" + "get",
            Response.Listener { response->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val akun = jsonArray.getJSONObject(i)
                    otpDb = akun.getString("otp_status")
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
}