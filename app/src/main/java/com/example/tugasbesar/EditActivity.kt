package com.example.tugasbesar

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import com.example.tugasbesar.databinding.ActivityEditBinding
import com.example.tugasbesar.room.Constant
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var mbunlde : Bundle
    private lateinit var binding: ActivityEditBinding
    private lateinit var usernameEdit : EditText
    private lateinit var passwordEdit : EditText
    private lateinit var emailEdit : EditText
    private lateinit var phoneEdit : EditText
    private lateinit var tanggalEdit : EditText
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var passworddb :String
    lateinit var usernamedb :String
    lateinit var emaildb :String
    lateinit var telpdb :String
    lateinit var tgldb :String
    private val calender = Calendar.getInstance()
    private val formatter = SimpleDateFormat("dd, MMM, yyyy", Locale.US)
    val db by lazy { UserDB(this) }
    lateinit var userid: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usernameEdit = binding.editUsername
        passwordEdit = binding.editPassword
        emailEdit = binding.editEmail
        phoneEdit = binding.editPhone
        tanggalEdit = binding.editTgl
        getBundle()
        autofill(vuser ,vpass)
        setText()
        tanggalEdit.setOnClickListener{
            DatePickerDialog(this,this,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().updateNote(
                    User(userid.toInt(), usernameEdit.getText().toString(),
                        passwordEdit.getText().toString(),emailEdit.getText().toString(),phoneEdit.getText().toString(),
                    tanggalEdit.getText().toString())
                )
                finish()
            }
            val intent = Intent(this,profile::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",usernameEdit.getText().toString())
            mBundle.putString("password",passwordEdit.getText().toString())
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }

        button_cancle.setOnClickListener {
            val intent = Intent(this,profile::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",usernameEdit.getText().toString())
            mBundle.putString("password",passwordEdit.getText().toString())
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
    fun setText(){
        edit_username.setText(usernamedb)
        edit_password.setText(passworddb)
        edit_email.setText(emaildb)
        edit_phone.setText(telpdb)
        edit_tgl.setText(tgldb)
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
}
