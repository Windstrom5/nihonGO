package com.example.tugasbesar

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.toolbox.Volley
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.example.tugasbesar.databinding.ActivityRegisterViewBinding
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import com.example.tugasbesar.models.Users
import com.example.tugasbesar.api.AkunApi
import com.google.gson.Gson
//import kotlinx.android.synthetic.main.activity_register_view.*

class RegisterView : AppCompatActivity() , DatePickerDialog.OnDateSetListener{
    private lateinit var register : TextView
    private lateinit var btnRegister : Button
    private lateinit var btnlogin : TextView
    private lateinit var userRegister : TextInputLayout
    private lateinit var passRegister : TextInputLayout
    private lateinit var emailRegister : TextInputLayout
    private lateinit var teleponRegister : TextInputLayout
    private lateinit var tanggalRegister : TextInputLayout
    private lateinit var confirmRegister : TextInputLayout
    private lateinit var tglView : TextInputEditText
    private lateinit var loading :LinearLayout
    lateinit var usernamedb :String
    var sharedPreferences: SharedPreferences? = null
    private var etUser:EditText? = null
    private var etPass:EditText? = null
    private var etEmail:EditText? = null
    private var etTelp:EditText? = null
    private var etDate:EditText? = null
    private var check:Boolean = false
    lateinit var passworddb :String
    private val calender = Calendar.getInstance()
    private val CHANNEL_ID_1 = "channel_notification_01"
    private val myPreference = "myPref"
    private val userkey = "userKey"
    private val passkey = "passwordKey"
    private val notificationId1 = 101
    private val formatter = SimpleDateFormat("dd, MMM, yyyy",Locale.US)
    val GROUP_KEY_WORK_EMAIL = "com.android.example.tugasbesar"
    val db by lazy { UserDB(this) }
    private var queue: RequestQueue? = null
    private lateinit var binding : ActivityRegisterViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
        binding = ActivityRegisterViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createNotificationChannel()
        getSupportActionBar()?.hide();
        setTitle("Register Page")
        setRegister()
        queue= Volley.newRequestQueue(this)
        etUser = binding.username
        etPass = binding.password
        etEmail = binding.email
        etTelp = binding.phone
        etDate = binding.tgl
        btnRegister = binding.regisButton
        btnlogin = binding.loginText
        userRegister = binding.userRegis
        passRegister =  binding.passwordRegis
        confirmRegister = binding.confirmRegis
        emailRegister = binding.emailRegis
        tanggalRegister = binding.tglRegis
        tglView = binding.tgl
        loading = findViewById(R.id.layout_loading)
        tanggalRegister.setStartIconOnClickListener(View.OnClickListener{
            DatePickerDialog(this,this,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
        })
        teleponRegister = binding.noRegis
        binding.regisButton.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val user : String = userRegister.getEditText()?.getText().toString()
            val pass : String = passRegister.getEditText()?.getText().toString()
            val confirmpass : String = confirmRegister.getEditText()?.getText().toString()
            val email : String = emailRegister.getEditText()?.getText().toString()
            val tgl : String = tanggalRegister.getEditText()?.getText().toString()
            val tlp :String = teleponRegister.getEditText()?.getText().toString()
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
            }else if(email.isEmpty()){
                emailRegister.setError("Email tidak boleh Kosong")
                checkLogin = false
            }else if(tlp.isEmpty()) {
                teleponRegister.setError("Nomor Telepon tidak boleh Kosong")
                checkLogin = false
            }else if(tgl.isEmpty()){
                tanggalRegister.setError("Tanggal Lahir tidak boleh Kosong")
                checkLogin = false
            }else if(checkUsername(userRegister.getEditText()?.getText().toString())) {
                userRegister.setError("Username Sudah Ada")
                checkLogin = false
            }else if(pass != confirmpass){
                confirmRegister.setError("Password Tidak Sama")
                checkLogin = false
            }else{
//                runBlocking(){
//                    val usernameDb = async {
//                        val Account: User? = db.noteDao().getUsername(userRegister.getEditText()?.getText().toString())
//                        if (Account != null) {
//                            Account.username
//                        } else {
//                            null
//                        }
//                    }
//                    usernamedb = usernameDb.await().toString()
//                }
//                if(userRegister.getEditText()?.getText().toString() == usernamedb){
//                    userRegister.setError("Username Sudah Ada")
//                }else{
//                    checkLogin=true
//                }
                checkLogin=true
            }
            if(!checkLogin) {
                return@OnClickListener
            }else{
                    createAccount()
                    sendNotification1()
                    val userSave: String =
                        userRegister.getEditText()?.getText().toString().trim()
                    val passSave: String =
                        passRegister.getEditText()?.getText().toString().trim()
                    sharedPreferences = getSharedPreferences(myPreference,
                        Context.MODE_PRIVATE)
                    val editor: SharedPreferences.Editor =
                        sharedPreferences!!.edit()
                    editor.putString(userkey, userSave)
                    editor.putString(passkey, passSave)
                    editor.apply()
                    val intent = Intent(this,MainActivity::class.java)
                    val mBundle = Bundle()
                    mBundle.putString("username",userRegister.getEditText()?.getText().toString())
                    mBundle.putString("password",passRegister.getEditText()?.getText().toString())
                    mBundle.putString("email",emailRegister.getEditText()?.getText().toString())
                    mBundle.putString("notelp",teleponRegister.getEditText()?.getText().toString())
                    mBundle.putString("tanggallahir",tanggalRegister.getEditText()?.getText().toString())
                    intent.putExtra("profile",mBundle)
                    startActivity(intent)
            }
        })

        btnlogin.setOnClickListener(){
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun setRegister(){
        register = binding.loginText
        register.setTextColor(Color.parseColor("#001eff"))
    }

    override fun onDateSet(view: DatePicker?, year:Int, month:Int , dayofMonth : Int) {
        Log.e("Calender","$year -- $month -- $dayofMonth")
        calender.set(year, month, dayofMonth)
        displayFormattedDate(calender.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long){
        findViewById<TextInputEditText>(R.id.tgl).setText(formatter.format(timestamp))
        Log.i("Formatting",timestamp.toString())
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "Notification Title"
            val descriptionText = "Notification Description"

            val channel1 = NotificationChannel(CHANNEL_ID_1, name, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel1)
        }
    }

    private fun sendNotification1(){
        val broadcastIntent : Intent = Intent(this, NotificationReceiver::class.java)
        broadcastIntent.putExtra("toastMessage", "welcome")
        val actionIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        val builder = NotificationCompat.Builder(this, CHANNEL_ID_1)
            .setSmallIcon(R.drawable.message_arigatou)
            .setContentTitle("User "+binding?.username?.text.toString())
            .setContentText("Have Been Registered")
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
            .setGroup(GROUP_KEY_WORK_EMAIL)
            .setColor(Color.YELLOW)
            .setStyle(NotificationCompat.BigPictureStyle()
                .bigPicture(BitmapFactory.decodeResource(resources,R.drawable.message_arigatou)))

        with(NotificationManagerCompat.from(this)){
            notify(notificationId1, builder.build())
        }
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

    private fun createAccount(){
        setLoading(true)
        val akun = Users(
            userRegister.getEditText()?.getText().toString(),
            passRegister.getEditText()?.getText().toString(),
            emailRegister.getEditText()?.getText().toString(),
            teleponRegister.getEditText()?.getText().toString(),
            tanggalRegister.getEditText()?.getText().toString()
        )
        val StringRequest:StringRequest = object : StringRequest(Method.POST,AkunApi.ADD_URL,
            Response.Listener { response ->
                val gson = Gson()
                val akun = gson.fromJson(response, Users::class.java)
                if(akun != null)
                    Toast.makeText(this@RegisterView,"Akun Berhasil Ditambahkan", Toast.LENGTH_SHORT).show()
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
                        this@RegisterView,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                }catch (e: Exception){
                    Toast.makeText(this@RegisterView,e.message, Toast.LENGTH_SHORT).show()
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
                params.put("username",userRegister.getEditText()?.getText().toString())
                params.put("password",passRegister.getEditText()?.getText().toString())
                params.put("email",emailRegister.getEditText()?.getText().toString())
                params.put("no_telp",teleponRegister.getEditText()?.getText().toString())
                params.put("birth_date",tanggalRegister.getEditText()?.getText().toString())
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

    private fun checkUsername(Username:String): Boolean{
        setLoading(true)
        val StringRequest: StringRequest = object
            : StringRequest(Method.GET, AkunApi.GET_ALL_URL,
            Response.Listener { response->
                val gson = Gson()

                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val akun = jsonArray.getJSONObject(i)
                    if(Username == akun.getString("username")){
                        check = true
                    }
                    setLoading(false)
                }
//                var akun : Users = gson.fromJson(jsonArray.toString(), Users::class.java)
//                Log.d("MainActivity","dbResponse: ${akun.username}")
//                userProfile!!.text=akun.username
//                emailProfile!!.text=akun.email
//                notelpProfile!!.text=akun.no_telp
//                birthProfile!!.text=akun.birth_date
////                getData(usernamedb.toString(),passworddb.toString(),emaildb.toString(),
////                    phonedb.toString(),tgldb.toString())
//                Toast.makeText(this,"Data Berhasil Diambil!", Toast.LENGTH_SHORT).show()
//                setLoading(false)
//            }, Response.ErrorListener { error->
//                setLoading(false)
//                try{
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this@profile,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }catch (e: Exception){
//                    Toast.makeText(this@profile,e.message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        ){
//            @Throws(AuthFailureError::class)
//            override fun getHeaders(): Map<String, String> {
//                val headers = HashMap<String,String>()
//                headers["Accept"] = "application/json"
//                return headers
//            }
//        }
//        queue!!.add(StringRequest)

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
        return check
    }
}