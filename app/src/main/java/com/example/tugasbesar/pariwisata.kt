package com.example.tugasbesar

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.tugasbesar.RV.RVPariwisataAdapter
import com.example.tugasbesar.databinding.ActivityPariwisataBinding
import com.example.tugasbesar.entity.entityPariwisata
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class pariwisata : AppCompatActivity() {
    var binding : ActivityPariwisataBinding? = null
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var vcity: String
    lateinit var mbunlde : Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPariwisataBinding.inflate(layoutInflater)
        getBundle()
        setContentView(binding?.root)
        MotionToast.Companion.darkToast(this,
            "Welcome to  "+vuser,
            "Successfully Entering!",
            MotionToastStyle.SUCCESS,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.helvetica_regular))
        val adapter = RVPariwisataAdapter(entityPariwisata.listnamaPariwisata)
        adapter.getVariable(vuser,vpass,vcity)
        binding?.rvPariwisata?.adapter = adapter
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
//        Log.d("CDA", "onBackPressed Called")
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this@pariwisata)
//        builder.setMessage("Want to log out?")
//            .setNegativeButton("No", object : DialogInterface.OnClickListener {
//                override fun onClick(dialogInterface: DialogInterface, i: Int) {
//
//                }
//            })
//            .setPositiveButton("YES", object : DialogInterface.OnClickListener {
//                override fun onClick(dialogInterface: DialogInterface, i: Int) {
//                    startActivity(Intent(this@pariwisata, MainActivity::class.java))
//                }
//            })
//            .show()
        Log.d("CDA", "onBackPressed Called")
        val intent = Intent(this,kota::class.java)
        val mBundle = Bundle()
        mBundle.putString("username",vuser)
        mBundle.putString("password",vpass)
        intent.putExtra("profile",mBundle)
        startActivity(intent)
    }
    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
                vcity = mbunlde.getString("city")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
        }
    }
}