package com.example.tugasbesar

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.tugasbesar.databinding.ActivityRegisterViewBinding
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

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
        btnRegister = binding.regisButton
        btnlogin = binding.loginText
        userRegister = binding.userRegis
        passRegister =  binding.passwordRegis
        confirmRegister = binding.confirmRegis
        emailRegister = binding.emailRegis
        tanggalRegister = binding.tglRegis
        tglView = binding.tgl
        tglView.setOnClickListener{
            DatePickerDialog(this,this,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
        }
        teleponRegister = binding.noRegis
        binding.regisButton.setOnClickListener(View.OnClickListener {
            var checkLogin = false
            val pass : String = passRegister.getEditText()?.getText().toString()
            val confirmpass : String = confirmRegister.getEditText()?.getText().toString()
            val user : String = userRegister.getEditText()?.getText().toString()
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
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().addNote(
                        User(0,userRegister.getEditText()?.getText().toString(),
                            passRegister.getEditText()?.getText().toString(),emailRegister.getEditText()?.getText().toString(),
                            teleponRegister.getEditText()?.getText().toString(),tanggalRegister.getEditText()?.getText().toString())
                    )
                    finish()
                }
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
        broadcastIntent.putExtra("toastMessage","welcome",)
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
}