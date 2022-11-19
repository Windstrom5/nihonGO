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
            }
            if (pass == confirmpass && user.isNotEmpty() && pass.isNotEmpty() && confirmpass.isNotEmpty()){
                runBlocking(){
                    val usernameDb = async {
                        val Account: User? = db.noteDao().getUsername(userRegister.getEditText()?.getText().toString())
                        if (Account != null) {
                            Account.username
                        } else {
                            null
                        }
                    }
                    usernamedb = usernameDb.await().toString()
                }
                if(userRegister.getEditText()?.getText().toString() == usernamedb){
                    userRegister.setError("Username Sudah Ada")
                }else{
                    checkLogin=true
                }
            }else if(pass != confirmpass){
                confirmRegister.setError("Password Tidak Sama")
            }
            if(!checkLogin) {
                return@OnClickListener
            }else{
                createAccount(user,pass,email,tlp,tgl)
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

    private fun createAccount(username:String,password:String,email:String,telepon:String,tanggal:String){
        setLoading(true)

        val akun = Users(
            username,password, email, telepon, tanggal
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
            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(akun)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(StringRequest)
    }
}