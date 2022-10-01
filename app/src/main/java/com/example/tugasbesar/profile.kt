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
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class profile : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    lateinit var userAdapter: MainAdapterProfile
    lateinit var mbunlde : Bundle
    lateinit var vuser : String
    lateinit var binding: ActivityProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getBundle()
        setupListener()
        onStart()
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        userAdapter = MainAdapterProfile(arrayListOf(), object : MainAdapterProfile.OnAdapterListener{
            override fun onClick(note: User) {
                //Toast.makeText(applicationContext, note.title, Toast.LENGTH_SHORT).show()
//               intentEdit(note.id, Constant.TYPE_READ)
            }

            override fun onUpdate(note: User) {
                intentEdit(note.id, Constant.TYPE_UPDATE)
            }

            override fun onDelete(note: User) {
                deleteDialog(note)
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
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
        }

    }

//
    fun setupListener() {
        button_update.setOnClickListener{
            intentEdit(0,Constant.TYPE_CREATE)
        }
    }

    fun intentEdit(noteId : Int, intentType: Int){
        startActivity(
            Intent(applicationContext, EditActivity::class.java).putExtra("intent_id", noteId).putExtra("intent_type", intentType)
        )
    }
}