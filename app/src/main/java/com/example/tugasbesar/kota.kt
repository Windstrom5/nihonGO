package com.example.tugasbesar

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.tugasbesar.RV.RVKotaAdapter
import com.example.tugasbesar.databinding.ActivityKotaBinding
import com.example.tugasbesar.entity.kota
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class kota : AppCompatActivity() {
    var binding : ActivityKotaBinding? = null
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var mbunlde : Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKotaBinding.inflate(layoutInflater)
        getBundle()
        setContentView(binding?.root)
        MotionToast.Companion.darkToast(this,
            "Success Enter NihongGo "+vuser,
            "Successfully Entering!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
            val adapter = RVKotaAdapter(kota.listofnamaKota)
            adapter.getVariable(vuser,vpass)
            binding?.rvKota?.adapter = adapter
    }
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        val layoutManager = LinearLayoutManager(context)
//        val adapter : RVKotaAdapter = RVKotaAdapter(kota.listofnamaKota)
//
//        rvKulinerAdapter.layoutManager = layoutManager
//        rvKulinerAdapter.setHasFixedSize(true)
//        rvKulinerAdapter.adapter = adapter

//    }
    override fun onBackPressed() {
        Log.d("CDA", "onBackPressed Called")
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@kota)
        builder.setMessage("Want to log out?")
        .setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface, i: Int) {

            }
        })
        .setPositiveButton("YES", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface, i: Int) {
                startActivity(Intent(this@kota, MainActivity::class.java))
            }
        })
        .show()

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