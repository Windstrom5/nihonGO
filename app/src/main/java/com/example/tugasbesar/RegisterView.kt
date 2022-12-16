package com.example.tugasbesar

import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.databinding.ActivityRegisterViewBinding
import com.example.tugasbesar.models.Users
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*


//import kotlinx.android.synthetic.main.activity_register_view.*

class RegisterView : AppCompatActivity() , DatePickerDialog.OnDateSetListener{
    companion object{
        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 101
        private const val TAG = "MAIN_TAG"
    }
    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>
    private lateinit var profileview: CircleImageView
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
    private lateinit var vergil : CircleImageView
    private lateinit var reaper : CircleImageView
    private lateinit var indihome : CircleImageView
    private lateinit var cassidy : CircleImageView
    private lateinit var goro : CircleImageView
    private lateinit var kiryu : CircleImageView
    private lateinit var amongus : CircleImageView
    private lateinit var armstrong : CircleImageView
    private lateinit var cj : CircleImageView
    private lateinit var lucy : CircleImageView
    private lateinit var dva : CircleImageView
    private lateinit var sam : CircleImageView
    private var profilePicture : String ?= null
    lateinit var usernamedb :String
    var sharedPreferences: SharedPreferences? = null
    private var imageUri: Uri? = null
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
        profileview = binding.profileView
        loading = findViewById(R.id.layout_loading)
        tanggalRegister.setStartIconOnClickListener(View.OnClickListener{
            DatePickerDialog(this,this,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
        })
        binding.profileView.setOnClickListener{
            createDialog()
        }
        teleponRegister = binding.noRegis
        binding.regisButton.setOnClickListener(View.OnClickListener {
            userRegister.setError(null)
            passRegister.setError(null)
            confirmRegister.setError(null)
            emailRegister.setError(null)
            tanggalRegister.setError(null)
            teleponRegister.setError(null)
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
            }else if(pass != confirmpass) {
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
            }else if(!isValidEmail(email)){
                emailRegister.setError("Format Email Salah")
                checkLogin = false
            }else if(tlp.isEmpty()) {
                teleponRegister.setError("Nomor Telepon tidak boleh Kosong")
                checkLogin = false
            }else if(tgl.isEmpty()){
                tanggalRegister.setError("Tanggal Lahir tidak boleh Kosong")
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
                        this@RegisterView,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                    userRegister.setError("Akun Sudah Ada")
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

    private fun checkUsername(Username:String){
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
                        userRegister.setError("Username Sudah Ada")
                        Toast.makeText(this@RegisterView,"Username Sudah Terdaftar",Toast.LENGTH_SHORT).show()
                        val intent = Intent(this,MainActivity::class.java)
                        startActivity(intent);
                        break
                    }else if(i==jsonArray.length()-1 &&Username != akun.getString("username")){
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
                        }
                        queue!!.add(StringRequest)
                    }
                    setLoading(false)
                }
            }, Response.ErrorListener { error->
                setLoading(false)
            }
        ){
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String,String>()
                headers["Accept"] = "application/json"
                return headers
            }
        }
        queue!!.add(StringRequest)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun createDialog(){
        val dialogBuilder = AlertDialog.Builder(this)
        val popup : View = layoutInflater.inflate(R.layout.bottom_sheet,null)
        val camera : TextView = popup.findViewById(R.id.cameraOption)
        val gallery: TextView = popup.findViewById(R.id.galleryOption)
        val default : TextView = popup.findViewById(R.id.defaultOption)
        dialogBuilder.setView(popup)
        val dialog = dialogBuilder.create()
        dialog.show()
        camera.setOnClickListener{
            if(checkCameraPermission()){
                pickImageCamera()
            }else{
                requestCameraPermission()
            }
        }
        gallery.setOnClickListener{
            if(checkStoragePermission()){
                pickImageGallery()
            }else{
                requestStoragePermission()
            }
        }
        default.setOnClickListener{
            dialog.dismiss()
            createDialogProfile()
        }
        dialog.dismiss()
    }

    private fun createDialogProfile(){
        val dialogBuilder = AlertDialog.Builder(this)
        val popup : View = layoutInflater.inflate(R.layout.profilepic_sheet,null)
        vergil = popup.findViewById(R.id.vergilView)
        reaper = popup.findViewById(R.id.reaperView)
        indihome = popup.findViewById(R.id.indihomeView)
        cassidy = popup.findViewById(R.id.cassidyView)
        goro = popup.findViewById(R.id.goroView)
        kiryu = popup.findViewById(R.id.kiryuView)
        amongus = popup.findViewById(R.id.amogusView)
        armstrong = popup.findViewById(R.id.nanomachineView)
        cj = popup.findViewById(R.id.cjView)
        lucy = popup.findViewById(R.id.lucyView)
        dva = popup.findViewById(R.id.dvaView)
        sam = popup.findViewById(R.id.sammgsView)
        val save : Button = popup.findViewById(R.id.button_saveImage)
        val cancel : Button = popup.findViewById(R.id.button_cancelImage)
        save.isEnabled = false
        dialogBuilder.setView(popup)
        val dialog = dialogBuilder.create()
        dialog.show()
        cancel.setOnClickListener{
            dialog.dismiss()
        }
        vergil.setOnClickListener{
            profilePicture = "vergil"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppvergil)
                dialog.dismiss()
            }
        }
        armstrong.setOnClickListener{
            profilePicture = "nanomachine"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppnanomachine)
                dialog.dismiss()
            }
        }
        reaper.setOnClickListener{
            profilePicture = "reaper"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppreaper)
                dialog.dismiss()
            }
        }
        indihome.setOnClickListener{
            profilePicture = "indihome"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppindihome)
                dialog.dismiss()
            }
        }
        cassidy.setOnClickListener{
            profilePicture = "cassidy"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppcassidy)
                dialog.dismiss()
            }
        }
        goro.setOnClickListener{
            profilePicture = "goro"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppgoro)
                dialog.dismiss()
            }
        }
        kiryu.setOnClickListener{
            profilePicture = "kiryu"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppkiryu)
                dialog.dismiss()
            }
        }
        amongus.setOnClickListener{
            profilePicture = "amogus"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppamogus)
                dialog.dismiss()
            }
        }
        cj.setOnClickListener{
            profilePicture = "carljohnson"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppcarljohnson)
                dialog.dismiss()
            }
        }
        lucy.setOnClickListener{
            profilePicture = "lucy"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.pplucy)
                dialog.dismiss()
            }
        }
        dva.setOnClickListener{
            profilePicture = "dva"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppdva)
                dialog.dismiss()
            }
        }
        sam.setOnClickListener{
            profilePicture = "sammgs"
            save.isEnabled = true
            save.setOnClickListener{
                binding.profileView.setImageResource(R.drawable.ppsammgs)
                dialog.dismiss()
            }
        }
    }

    private fun checkCameraPermission():Boolean{
        val resultCamera = (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED)
        val resultStorage = (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        return resultCamera && resultStorage
    }

    private fun requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission, RegisterView.CAMERA_REQUEST_CODE)
    }

    private fun checkStoragePermission():Boolean{
        val result = (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        return result
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission, RegisterView.STORAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            RegisterView.CAMERA_REQUEST_CODE ->{
                if(grantResults.isNotEmpty()){
                    val cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                    if(cameraAccepted&&storageAccepted){
                        pickImageCamera()
                    }else{
                        Toast.makeText(this,"Camera And Storage Permission Are Required",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            RegisterView.STORAGE_REQUEST_CODE ->{
                val storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if(storageAccepted){
                    pickImageGallery()
                }else{
                    Toast.makeText(this,"Storage Permission Is Required",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun pickImageCamera(){
        val contentValues = ContentValues()
        contentValues.put(MediaStore.Images.Media.TITLE,"Foto Sample")
        contentValues.put(MediaStore.Images.Media.DESCRIPTION,"Deskripsi Foto Sample")

        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,contentValues)

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri)

        cameraActivityResultLauncher.launch(intent)
    }

    private val cameraActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data
//            imageUri = data!!.data
//            imageUri = intent.getData();
            Log.d(RegisterView.TAG,"cameraActivityResultLauncher: imageUri: $imageUri")
            binding.profileView.setImageURI(imageUri)
        }
    }

    private fun pickImageGallery(){
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryActivityResultLauncher.launch(intent)
    }

    private val galleryActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){result->
        if(result.resultCode == Activity.RESULT_OK){
            val data = result.data
            imageUri = data!!.data
            Log.d(RegisterView.TAG,"galleryActivityResultLauncher: imageUri: $imageUri")
            binding.profileView.setImageURI(imageUri)
        }
    }
}