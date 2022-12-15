package com.example.tugasbesar

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.camera.CameraActivity
import com.example.tugasbesar.databinding.ActivityProfileBinding
import com.example.tugasbesar.models.Users
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject

class profile : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private lateinit var userAdapter: MainAdapterProfile
    private lateinit var mbunlde : Bundle
    private lateinit var vuser : String
    private lateinit var vpass : String
    private lateinit var vcity : String
    private lateinit var vcategory : String
    private lateinit var passworddb :String
    private var userProfile:TextView? = null
    private var emailProfile:TextView? = null
    private var notelpProfile:TextView? = null
    private var birthProfile:TextView? = null
    lateinit var usernamedb :String
    lateinit var emaildb :String
    lateinit var telpdb :String
    lateinit var tgldb :String
    lateinit var userid: String
    private lateinit var loading : LinearLayout
    private var queue: RequestQueue? = null
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()
//        autofill(vuser ,vpass)
//        onStart()
//        Toast.makeText(this,"Welcome "+vuser,Toast.LENGTH_SHORT).show()
        loading = findViewById(R.id.layout_loading)
        queue= Volley.newRequestQueue(this)
        //Tampil PROFILE
        userProfile = binding.namaProfil
        emailProfile= binding.emailProfil
        notelpProfile= binding.notelpProfil
        birthProfile= binding.birthProfil
        getAkun(vuser,vpass)
      //  setupRecyclerView()
        button_update.setOnClickListener(){
            val intent = Intent(this,EditActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",vuser)
            mBundle.putString("password",vpass)
            mBundle.putString("city",vcity)
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }
        profileView.setOnClickListener(){
//            changeFragment(DialogImageChooser())
//            true
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        button_delete.setOnClickListener {
//            CoroutineScope(Dispatchers.IO).launch {
//                db.noteDao().deleteNote(
//                    User(userid.toInt(), usernamedb,
//                        passworddb,emaildb,telpdb,
//                        tgldb)
//                )
//                finish()
//            }
            val intent = Intent(this,MapsActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onBackPressed() {
//        finish()
        val intent = Intent(this,itemActivity::class.java)
        val mBundle = Bundle()
        mBundle.putString("username",vuser)
        mBundle.putString("password",vpass)
        mBundle.putString("city", vcity)
        mBundle.putString("category",vcategory)
        intent.putExtra("profile",mBundle)
        startActivity(intent)
    }

//    fun changeFragment(fragment: Fragment?) {
//        if (fragment != null) {
//            getSupportFragmentManager()
//                .beginTransaction()
//                .replace(R.id.layout_fragment, fragment)
//                .commit()
//        }
//    }

//    private fun setupRecyclerView() {
//        userAdapter = MainAdapterProfile(arrayListOf(), object : MainAdapterProfile.OnAdapterListener{
//            override fun onClick(note: User) {
//                //Toast.makeText(applicationContext, note.title, Toast.LENGTH_SHORT).show()
////               intentEdit(note.id, Constant.TYPE_READ)
//            }
//
//           override fun onUpdate(note: User) {
////                intentEdit(note.id, Constant.TYPE_UPDATE)
//            }
//
//            override fun onDelete(note: User) {
////                deleteDialog(note)
//            }
//        })
//        list_note.apply {
//            layoutManager = LinearLayoutManager(applicationContext)
//            adapter = userAdapter
//        }
//    }

    private fun deleteDialog(user: User){
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.apply {
            setTitle("Confirmation")
            setMessage("Are You Sure to delete this data From ${user.username}?")
            setNegativeButton("Cancel", DialogInterface.OnClickListener{
                    dialogInterface, i -> dialogInterface.dismiss()
            })
            setPositiveButton("Delete", DialogInterface.OnClickListener{
                    dialogInterface, i -> dialogInterface.dismiss()
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().deleteNote(user)
//                    loadData(vuser)
                }
            })
        }
        alertDialog.show()
    }

//    override fun onStart() {
//        super.onStart()
////        loadData(vuser)
//    }

//    fun loadData(vuser : String) {
//        CoroutineScope(Dispatchers.IO).launch  {
//            val notes = db.noteDao().getUser(vuser)
//            Log.d("MainActivity","dbResponse: $notes")
//            withContext(Dispatchers.Main){
//                userAdapter.setData(notes)
//            }
//        }
//    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
                vcity = mbunlde.getString("city")!!
                vcategory = mbunlde.getString("category")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
            vcity = ""
        }

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



//
//    fun setupListener() {
//        button_update.setOnClickListener{
//            intentEdit(0,Constant.TYPE_CREATE)
//        }
//    }
//
//    fun intentEdit(noteId : Int, intentType: Int){
//        startActivity(
//            Intent(applicationContext, EditActivity::class.java).putExtra("intent_id", noteId).putExtra("intent_type", intentType)
//        )
//    }

    private fun getAkun(Username:String,Password:String){
        setLoading(true)
        val StringRequest: StringRequest = object
            : StringRequest(Method.GET, AkunApi.GET_BY_USERNAME + Username + "/" + Password,
            Response.Listener { response->
                val gson = Gson()

                val jsonObject = JSONObject(response)
                val jsonArray = jsonObject.getJSONArray("data")
                for (i in 0 until jsonArray.length()) {
                    val akun = jsonArray.getJSONObject(i)
                    binding.namaProfil.setText(akun.getString("username"))
                    binding.emailProfil.setText(akun.getString("email"))
                    binding.notelpProfil.setText(akun.getString("no_telp"))
                    binding.birthProfil.setText(akun.getString("birth_date"))
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
}