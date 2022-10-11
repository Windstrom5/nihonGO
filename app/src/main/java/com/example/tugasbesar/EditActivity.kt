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
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.*
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class EditActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {
    lateinit var mbunlde : Bundle
    private lateinit var binding: ActivityEditBinding
    private lateinit var usernameEdit : TextInputLayout
    private lateinit var passwordEdit : TextInputLayout
    private lateinit var emailEdit : TextInputLayout
    private lateinit var phoneEdit : TextInputLayout
    private lateinit var tanggalEdit : TextInputLayout
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
        usernameEdit = binding.userInput
        passwordEdit = binding.passInput
        emailEdit = binding.emailInput
        phoneEdit = binding.phoneInput
        tanggalEdit = binding.tglInput
        getBundle()
        autofill(vuser ,vpass)
        setText()
        tanggalEdit.setStartIconOnClickListener(View.OnClickListener{
            DatePickerDialog(this,this,calender.get(Calendar.YEAR),calender.get(Calendar.MONTH),calender.get(Calendar.DAY_OF_MONTH)).show()
        })
        button_update.setOnClickListener {
            runBlocking(){
                val usernameDb = async {
                    val Account: User? = db.noteDao().getUsername(usernameEdit.getEditText()?.getText().toString())
                    if (Account != null) {
                        Account.username
                    } else {
                        null
                    }
                }
                usernamedb = usernameDb.await().toString()
            }
            if(usernameEdit.getEditText()?.getText().toString() == usernamedb && usernameEdit.getEditText()?.getText().toString()!=vuser){
                usernameEdit.setError("Username Sudah Ada")
            }else{
                CoroutineScope(Dispatchers.IO).launch {
                    db.noteDao().updateNote(
                        User(userid.toInt(), usernameEdit.getEditText()?.getText().toString(),
                            passwordEdit.getEditText()?.getText().toString(),emailEdit.getEditText()?.getText().toString(),
                            phoneEdit.getEditText()?.getText().toString(), tanggalEdit.getEditText()?.getText().toString())
                    )
                    finish()
                }
                val intent = Intent(this,profile::class.java)
                val mBundle = Bundle()
                mBundle.putString("username",usernameEdit.getEditText()?.getText().toString())
                mBundle.putString("password",passwordEdit.getEditText()?.getText().toString())
                intent.putExtra("profile",mBundle)
                startActivity(intent)
            }
        }

        button_cancel.setOnClickListener {
            val intent = Intent(this,profile::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",usernameEdit.getEditText()?.getText().toString())
            mBundle.putString("password",passwordEdit.getEditText()?.getText().toString())
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
