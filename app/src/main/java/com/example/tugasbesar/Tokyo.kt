package com.example.tugasbesar

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tugasbesar.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class Tokyo : AppCompatActivity() {
    private lateinit var botNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tokyo)
        changeFragment(FragmentTempatWisata())
        botNav = findViewById(R.id.botNav)
        botNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menuWisata -> {
                    changeFragment(FragmentTempatWisata())
                    true
                }R.id.menuAkomodasi-> {
                changeFragment(FragmentAkomodasi())
                true
            }R.id.menuKuliner-> {
                changeFragment(FragmentKuliner())
                true
            }R.id.menuEvent->{
                changeFragment(FragmentEvent())
                true
            }else -> false
            }
        }
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
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setMessage("Wanna Logout Ma Brudda?")
                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {

                    }
                })
                .setPositiveButton("YES"){ dialog, which ->
//                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
//                        finishAndRemoveTask()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
//                    }
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
        else if(item.itemId == R.id.menuKota) {
            val intent = Intent(this, kota::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.menuProfile){
            val intent = Intent(this, profile::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}