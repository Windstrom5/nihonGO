package com.example.tugasbesar

import android.content.DialogInterface
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.tugasbesar.entity.kota

class home : AppCompatActivity() {
    private lateinit var kotatext: Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        changeFragment(FragmentTempatWisata())
    }

    fun changeFragment(fragment: Fragment?) {
        if (fragment != null) {
            getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.layout_fragment, fragment)
                .commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.menu.and(R.menu.menu2) ,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menulogout) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@home)
            builder.setMessage("Wanna Logout Ma Nibba?")
                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {

                    }
                })
                .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        finishAndRemoveTask()
                    }
                })
                .show()
        }else if(item.itemId == R.id.menuKota) {
            changeFragment(fragmentKota())
        }
        return super.onOptionsItemSelected(item)
    }

//    override fun onCreateOptionMenu(menu2: Menu):{
//        val menuInflater = MenuInflater(this)
//        menuInflater.inflate(R.menu.menu2, menu2)
//        return true
//    }
//
//    override fun onOptionsItemSelected(item: MenuItem): Boolean{
//
//        return super.onOptionsItemSelected(item)
//    }
}