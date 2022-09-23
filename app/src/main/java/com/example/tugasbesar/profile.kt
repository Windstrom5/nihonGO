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
import com.example.tugasbesar.room.User
import com.example.tugasbesar.room.UserDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class profile : AppCompatActivity() {
    val db by lazy { UserDB(this) }
    lateinit var userAdapter: MainAdapterProfile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onStart()
//        setupRecyclerView()
    }

//    private fun setupRecyclerView() {
//        Users = NoteAdapter(arrayListOf(), object : NoteAdapter.OnAdapterListener{
//            override fun onClick(note: Note) {
//                //Toast.makeText(applicationContext, note.title, Toast.LENGTH_SHORT).show()
//                intentEdit(note.id, Constant.TYPE_READ)
//            }
//
//            override fun onUpdate(note: Note) {
//                intentEdit(note.id, Constant.TYPE_UPDATE)
//            }
//
//            override fun onDelete(note: Note) {
//                deleteDialog(note)
//            }
//        })
//        list_note.apply {
//            layoutManager = LinearLayoutManager(applicationContext)
//            adapter = noteAdapter
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
                    loadData()
                }
            })
        }
        alertDialog.show()
    }

    override fun onStart() {
        super.onStart()
        loadData()
    }

    fun loadData() {
        CoroutineScope(Dispatchers.IO).launch  {
            val notes = db.noteDao().getNotes()
            Log.d("MainActivity","dbResponse: $notes")
            withContext(Dispatchers.Main){
                userAdapter.setData( notes )
            }
        }
    }


//    fun setupListener() {
//        button_create.setOnClickListener{
//            intentEdit(0,Constant.TYPE_CREATE)
//        }
//    }

//    fun intentEdit(noteId : Int, intentType: Int){
//        startActivity(
//            Intent(applicationContext, EditActivity::class.java).putExtra("intent_id", noteId).putExtra("intent_type", intentType)
//        )
//    }
}