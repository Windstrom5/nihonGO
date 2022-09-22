package com.example.tugasbesar

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.tugasbesar.fragment.FragmentAkomodasi
import com.example.tugasbesar.fragment.FragmentEvent
import com.example.tugasbesar.fragment.FragmentKuliner
import com.example.tugasbesar.fragment.FragmentTempatWisata
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
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@Tokyo)
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
        }
//        else if(item.itemId == R.id.menuKota) {
////            changeFragment(fragmentKota())
//        }
        return super.onOptionsItemSelected(item)
    }
}