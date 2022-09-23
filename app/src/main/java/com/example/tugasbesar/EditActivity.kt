package com.example.tugasbesar

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.tugasbesar.room.Constant
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import kotlinx.android.synthetic.main.activity_edit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditActivity : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    private var userid: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setupView()
        setupListener()
//
// Toast.makeText(this, noteId.toString(),Toast.LENGTH_SHORT).show()
    }
    fun setupView(){
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        val intentType = intent.getIntExtra("intent_type", 0)
        when (intentType){
//            Constant.TYPE_CREATE -> {
//                button_update.visibility = View.GONE
//            }
//            Constant.TYPE_READ -> {
//                button_save.visibility = View.GONE
//                button_update.visibility = View.GONE
//                getNote()
//            }
            Constant.TYPE_UPDATE -> {
                button_save.visibility = View.GONE
                getNote()
            }
        }
    }
    private fun setupListener() {
        button_save.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().addNote(
                    User(0,edit_username.text.toString(),
                        edit_password.text.toString(),edit_email.toString(),edit_phone.toString(),edit_tgl.toString())
                )
                finish()
            }
        }
        button_update.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                db.noteDao().updateNote(
                    User(0,edit_username.text.toString(),
                        edit_password.text.toString(),edit_email.toString(),edit_phone.toString(),edit_tgl.toString())
                )
                finish()
            }
        }
    }
    fun getNote() {
        userid = intent.getIntExtra("intent_id", 0)
        CoroutineScope(Dispatchers.IO).launch {
            val notes = db.noteDao().getNote(userid)[0]
            edit_username.setText(notes.username)
            edit_password.setText(notes.password)
            edit_email.setText(notes.email)
            edit_phone.setText(notes.noTelp)
            edit_tgl.setText(notes.tanggallahir)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}
