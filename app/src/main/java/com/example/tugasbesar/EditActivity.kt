package com.example.tugasbesar

import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.databinding.ActivityEditBinding
import com.example.tugasbesar.models.Users
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.*
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*


class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    companion object{
        private const val CAMERA_REQUEST_CODE = 100
        private const val STORAGE_REQUEST_CODE = 101
        private const val TAG = "MAIN_TAG"
    }
    private lateinit var cameraPermission: Array<String>
    private lateinit var storagePermission: Array<String>
    lateinit var mbunlde : Bundle
    private lateinit var binding: ActivityEditBinding
    private lateinit var profile_view: CircleImageView
    private lateinit var usernameEdit : TextInputLayout
    private lateinit var passwordEdit : TextInputLayout
    private lateinit var emailEdit : TextInputLayout
    private lateinit var phoneEdit : TextInputLayout
    private lateinit var tanggalEdit : TextInputLayout
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
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var vcity : String
    private var imageUri: Uri? = null
    lateinit var passworddb :String
    lateinit var usernamedb :String
    lateinit var emaildb :String
    lateinit var telpdb :String
    lateinit var tgldb :String
    private var profilePicture : String ?= null
    private lateinit var bitmap: Bitmap
    private lateinit var loading : LinearLayout
    private var blob:ByteArray ?=null
    private var queue: RequestQueue? = null
    private val calender = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd, MMM, yyyy", Locale.US)
    val db by lazy { UserDB(this) }
    lateinit var userid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loading = findViewById(R.id.layout_loading)
        usernameEdit = binding.userInput
        passwordEdit = binding.passInput
        emailEdit = binding.emailInput
        phoneEdit = binding.phoneInput
        tanggalEdit = binding.tglInput
        profile_view = binding.profileView
        val user : String = usernameEdit.getEditText()?.getText().toString()
        val pass : String = passwordEdit.getEditText()?.getText().toString()
        val email : String = emailEdit.getEditText()?.getText().toString()
        val phone : String = phoneEdit.getEditText()?.getText().toString()
        val tgl : String = tanggalEdit.getEditText()?.getText().toString()
        queue= Volley.newRequestQueue(this)
        getBundle()
        getAkun(vuser,vpass)
//        autofill(vuser ,vpass)
        tanggalEdit.setStartIconOnClickListener(View.OnClickListener{
            DatePickerDialog(this,this,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
        })
        profile_view.setOnClickListener{
            createDialog()
        }
        button_update.setOnClickListener {
            updateAccount(vuser)
//            updateUser(user,pass,email,tgl,phone)
//            runBlocking(){
//                val usernameDb = async {
//                    val Account: User? = db.noteDao().getUsername(usernameEdit.getEditText()?.getText().toString())
//                    if (Account != null) {
//                        Account.username
//                    } else {
//                        null
//                    }
//                }
//                usernamedb = usernameDb.await().toString()
//            }
//            if(usernameEdit.getEditText()?.getText().toString() == usernamedb && usernameEdit.getEditText()?.getText().toString()!=vuser){
//                usernameEdit.setError("Username Sudah Ada")
//            }else{
//                CoroutineScope(Dispatchers.IO).launch {
//                    db.noteDao().updateNote(
//                        User(userid.toInt(), usernameEdit.getEditText()?.getText().toString(),
//                            passwordEdit.getEditText()?.getText().toString(),emailEdit.getEditText()?.getText().toString(),
//                            phoneEdit.getEditText()?.getText().toString(), tanggalEdit.getEditText()?.getText().toString())
//                    )
//                    finish()
//                }
//            }
        }

        button_cancel.setOnClickListener {
            val intent = Intent(this,profile::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",usernameEdit.getEditText()?.getText().toString())
            mBundle.putString("password",passwordEdit.getEditText()?.getText().toString())
            mBundle.putString("city",vcity)
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }
    }
    override fun onDateSet(view: DatePicker?, year:Int, month:Int, dayofMonth : Int) {
        Log.e("Calender","$year -- $month -- $dayofMonth")
        calender.set(year, month, dayofMonth)
        displayFormattedDate(calender.timeInMillis)
    }

    private fun displayFormattedDate(timestamp: Long){
        findViewById<EditText>(R.id.edit_tgl).setText(formatter.format(timestamp))
        Log.i("Formatting",timestamp.toString())
    }

//    private fun autofill(user : String ,pass : String){
//        runBlocking(){
//            var idDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                if (Account != null) {
//                    Account.id
//                } else {
//                    null
//                }
//            }
//            val usernameDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                if (Account != null) {
//                    Account.username
//                } else {
//                    null
//                }
//            }
//            val passwordDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                Log.d("MainActivity","dbResponse: $Account")
//                if (Account != null) {
//                    Account.password
//                } else {
//                    null
//                }
//            }
//            val emailDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                Log.d("MainActivity","dbResponse: $Account")
//                if (Account != null) {
//                    Account.email
//                } else {
//                    null
//                }
//            }
//            val phoneDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                Log.d("MainActivity","dbResponse: $Account")
//                if (Account != null) {
//                    Account.noTelp
//                } else {
//                    null
//                }
//            }
//            val tanggalDb = async {
//                val Account: User? = db.noteDao().getAccount(user, pass)
//                Log.d("MainActivity","dbResponse: $Account")
//                if (Account != null) {
//                    Account.tanggallahir
//                } else {
//                    null
//                }
//            }
//            userid = idDb.await().toString()
//            usernamedb = usernameDb.await().toString()
//            passworddb = passwordDb.await().toString()
//            emaildb = emailDb.await().toString()
//            telpdb = phoneDb.await().toString()
//            tgldb = tanggalDb.await().toString()
//        }
//    }
//    fun setText(){
//        edit_username.setText(usernamedb)
//        edit_password.setText(passworddb)
//        edit_email.setText(emaildb)
//        edit_phone.setText(telpdb)
//        edit_tgl.setText(tgldb)
//    }
    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
                vcity = mbunlde.getString("city")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
            vcity = ""
        }

    }

    private fun getAkun(Username:String,Password:String){
        setLoading(true)
        val StringRequest: StringRequest = object
            : StringRequest(
            Method.GET, AkunApi.GET_BY_USERNAME + Username + "/" + Password,
            Response.Listener { response->
                val gson = Gson()
                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val akun = jsonArray.getJSONObject(i)
                    binding.userInput.getEditText()!!.setText(akun.getString("username"))
                    binding.passInput.getEditText()!!.setText(akun.getString("password"))
                    binding.emailInput.getEditText()!!.setText(akun.getString("email"))
                    binding.phoneInput.getEditText()!!.setText(akun.getString("no_telp"))
                    binding.tglInput.getEditText()!!.setText(akun.getString("birth_date"))
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

//    private fun updateUser(username:String,password:String,email:String,
//    no_telp:String,tgl_lahir:String){
//        val user = Users(username,password,email,no_telp,tgl_lahir)
//
//        val stringRequest: StringRequest =
//            object: StringRequest(Method.PUT, AkunApi.UPDATE_URL + username, Response.Listener { response ->
//                val jsonObject = JSONObject(response)
//                Toast.makeText(this, "Update User Success", Toast.LENGTH_SHORT).show()
//                    finish()
//            }, Response.ErrorListener { error ->
//                try{
//                    val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
//                    val errors = JSONObject(responseBody)
//                    Toast.makeText(
//                        this,
//                        errors.getString("message"),
//                        Toast.LENGTH_SHORT
//                    ).show()
//                } catch (e: Exception){
//                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//                }
//            }){
//                @Throws(AuthFailureError::class)
//                override fun getHeaders(): Map<String, String> {
//                    val headers = HashMap<String, String>()
//                    headers["Accept"] = "application/json"
//                    return headers
//                }
//
//                @Throws(AuthFailureError::class)
//                override fun getBody(): ByteArray {
//                    val gson = Gson()
//                    val requestBody = gson.toJson(user)
//                    return requestBody.toByteArray(StandardCharsets.UTF_8)
//                }
//
//                override fun getBodyContentType(): String {
//                    return "application/json"
//                }
//            }
//
//        queue!!.add(stringRequest)
//    }

    private fun updateAccount(username : String){
        setLoading(true)
        val akun = Users(
            usernameEdit.getEditText()?.getText().toString(),
            passwordEdit.getEditText()?.getText().toString(),
            emailEdit.getEditText()?.getText().toString(),
            phoneEdit.getEditText()?.getText().toString(),
            tanggalEdit.getEditText()?.getText().toString()
        )
        val StringRequest:StringRequest = object : StringRequest(Method.PUT,AkunApi.UPDATE_URL + username,
            Response.Listener { response ->
                val gson = Gson()
                val akun = gson.fromJson(response, Users::class.java)
                if(akun != null)
                    Toast.makeText(this@EditActivity,"Akun Berhasil Diedit", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this,profile::class.java)
                    val mBundle = Bundle()
                    mBundle.putString("username",usernameEdit.getEditText()?.getText().toString())
                    mBundle.putString("password",passwordEdit.getEditText()?.getText().toString())
                    mBundle.putString("city",vcity)
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
                        this@EditActivity,
                        errors.getString("message"),
                        Toast.LENGTH_SHORT
                    ).show()
                    usernameEdit.setError("Akun Sudah Ada")
                }catch (e: Exception){
                    Toast.makeText(this@EditActivity,e.message, Toast.LENGTH_SHORT).show()
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
                params.put("username",usernameEdit.getEditText()?.getText().toString())
                params.put("password",passwordEdit.getEditText()?.getText().toString())
                params.put("email",emailEdit.getEditText()?.getText().toString())
                params.put("no_telp",phoneEdit.getEditText()?.getText().toString())
                params.put("birth_date",tanggalEdit.getEditText()?.getText().toString())
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
    }

    private fun createDialogProfile(){
        val dialogBuilder = AlertDialog.Builder(this)
        val popup : View = layoutInflater.inflate(R.layout.profilepic_sheet,null)
        vergil = popup.findViewById(R.id.vergilView)
        val reaper : CircleImageView = popup.findViewById(R.id.reaperView)
        val indihome : CircleImageView = popup.findViewById(R.id.indihomeView)
        val cassidy : CircleImageView = popup.findViewById(R.id.cassidyView)
        val goro : CircleImageView = popup.findViewById(R.id.goroView)
        val kiryu : CircleImageView = popup.findViewById(R.id.kiryuView)
        val amongus : CircleImageView = popup.findViewById(R.id.amogusView)
        val armstrong : CircleImageView = popup.findViewById(R.id.nanomachineView)
        val cj : CircleImageView = popup.findViewById(R.id.cjView)
        val lucy : CircleImageView = popup.findViewById(R.id.lucyView)
        val dva : CircleImageView = popup.findViewById(R.id.dvaView)
        val sam : CircleImageView = popup.findViewById(R.id.sammgsView)
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
        ActivityCompat.requestPermissions(this,cameraPermission, CAMERA_REQUEST_CODE)
    }

    private fun checkStoragePermission():Boolean{
        val result = (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED)
        return result
    }

    private fun requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission, STORAGE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            CAMERA_REQUEST_CODE ->{
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
            STORAGE_REQUEST_CODE->{
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
            Log.d(TAG,"cameraActivityResultLauncher: imageUri: $imageUri")
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
            Log.d(TAG,"galleryActivityResultLauncher: imageUri: $imageUri")
            binding.profileView.setImageURI(imageUri)
        }
    }

    fun getBytesFromBitmap(bitmap: Bitmap): ByteArray? {
        if (bitmap != null) {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
            return stream.toByteArray()
        }
        return null
    }
}
