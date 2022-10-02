package com.example.tugasbesar

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tugasbesar.databinding.ActivityProfileBinding
import com.example.tugasbesar.room.Constant
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.button_update
import kotlinx.coroutines.*

class profile : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    lateinit var userAdapter: MainAdapterProfile
    lateinit var mbunlde : Bundle
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var passworddb :String
    lateinit var usernamedb :String
    lateinit var emaildb :String
    lateinit var telpdb :String
    lateinit var tgldb :String
    lateinit var userid: String
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()
        autofill(vuser ,vpass)
        onStart()
        setupRecyclerView()
        button_update.setOnClickListener(){
            val intent = Intent(this,EditActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",vuser)
            mBundle.putString("password",vpass)
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }

        button_delete.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().deleteNote(
                    User(userid.toInt(), usernamedb,
                        passworddb,emaildb,telpdb,
                        tgldb)
                )
                finish()
            }
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupRecyclerView() {
        userAdapter = MainAdapterProfile(arrayListOf(), object : MainAdapterProfile.OnAdapterListener{
            override fun onClick(note: User) {
                //Toast.makeText(applicationContext, note.title, Toast.LENGTH_SHORT).show()
//               intentEdit(note.id, Constant.TYPE_READ)
            }

           override fun onUpdate(note: User) {
//                intentEdit(note.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(note: User) {
//                deleteDialog(note)
            }
        })
        list_note.apply {
            layoutManager = LinearLayoutManager(applicationContext)
            adapter = userAdapter
        }
    }

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
                    loadData(vuser)
                }
            })
        }
        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        loadData(vuser)
    }

    fun loadData(vuser : String) {
        CoroutineScope(Dispatchers.IO).launch  {
            val notes = db.noteDao().getUser(vuser)
            Log.d("MainActivity","dbResponse: $notes")
            withContext(Dispatchers.Main){
                userAdapter.setData(notes)
            }
        }
    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
        }

    }

    private fun autofill(user : String ,pass : String){
        runBlocking(){
            var idDb = async {
                val Account: User? = db.noteDao().getAccount(user, pass)
                if (Account != null) {
                    Account.id
                } else {
                    null
                }
            }
            val usernameDb = async {
                val Account: User? = db.noteDao().getAccount(user, pass)
                if (Account != null) {
                    Account.username
                } else {
                    null
                }
            }
            val passwordDb = async {
                val Account: User? = db.noteDao().getAccount(user, pass)
                Log.d("MainActivity","dbResponse: $Account")
                if (Account != null) {
                    Account.password
                } else {
                    null
                }
            }
            val emailDb = async {
                val Account: User? = db.noteDao().getAccount(user, pass)
                Log.d("MainActivity","dbResponse: $Account")
                if (Account != null) {
                    Account.email
                } else {
                    null
                }
            }
            val phoneDb = async {
                val Account: User? = db.noteDao().getAccount(user, pass)
                Log.d("MainActivity","dbResponse: $Account")
                if (Account != null) {
                    Account.noTelp
                } else {
                    null
                }
            }
            val tanggalDb = async {
                val Account: User? = db.noteDao().getAccount(user, pass)
                Log.d("MainActivity","dbResponse: $Account")
                if (Account != null) {
                    Account.tanggallahir
                } else {
                    null
                }
            }
            userid = idDb.await().toString()
            usernamedb = usernameDb.await().toString()
            passworddb = passwordDb.await().toString()
            emaildb = emailDb.await().toString()
            telpdb = phoneDb.await().toString()
            tgldb = tanggalDb.await().toString()
        }
    }



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
}