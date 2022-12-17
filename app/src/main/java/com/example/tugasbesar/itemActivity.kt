package com.example.tugasbesar

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.RV.ItemAdapter
import com.example.tugasbesar.api.AkunApi
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.entity.itemList
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.item_list.*
import org.json.JSONObject
import java.nio.charset.StandardCharsets


class itemActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private var srItem: SwipeRefreshLayout?= null
    private var adapter: ItemAdapter? = null
    private var svItem: SearchView? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null
    private lateinit var vuser:String
    private lateinit var vpass:String
    private lateinit var vcity:String
    private lateinit var vcategory:String
    private lateinit var vemail : String
    lateinit var mbunlde : Bundle
    private lateinit var drawer : DrawerLayout
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView : NavigationView
    private lateinit var usernameView:TextView

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)
        getBundle()
        toolbar = findViewById(R.id.toolbar)
        drawer =findViewById(R.id.drawer_layout)
        navigationView = findViewById(R.id.nav_view)
        val headerView = navigationView.getHeaderView(0)
        val navUsername = headerView.findViewById(R.id.username_show) as TextView
        val navEmail = headerView.findViewById(R.id.email_show) as TextView
        navUsername.setText(vuser)
        if(vuser.equals("Admin")){
            navEmail.setText("Admin Page")
        }else{
            navEmail.setText("Welcome "+vuser)
        }
        navigationView.setNavigationItemSelectedListener(this)
        setSupportActionBar(toolbar);
        toggle = ActionBarDrawerToggle(this,drawer,toolbar,
        R.string.navigation_drawer_open,R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true);
        queue = Volley.newRequestQueue(this)
        layoutLoading = findViewById(R.id.layout_loading)
        srItem = findViewById(R.id.sr_item)
        svItem = findViewById(R.id.sv_item)
        srItem?.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener { allItem(vcategory,vcity) })
        svItem?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }
            override fun onQueryTextChange(s: String): Boolean {
                adapter!!.filter.filter(s)
                return false
            }
        })
        val rvItem = findViewById<RecyclerView>(R.id.rv_item)
        adapter = ItemAdapter(ArrayList(),this)
        adapter!!.getVariable(vuser,vpass,vcity,vcategory)
        rvItem.layoutManager = LinearLayoutManager(this)
        rvItem.adapter= adapter
        allItem(vcategory,vcity)
    }

    private fun allItem(category:String,kota : String){
        if(category == "Wisata"){
            srItem!!.isRefreshing=true
            val StringRequest: StringRequest = object : StringRequest(Method.GET, tempatWisataApi.GET_BY_NAMA_URL + kota,
                Response.Listener { response->
                    val gson = Gson()
                    val jsonObject = JSONObject(response)
                    val jsonArray = jsonObject.getJSONArray("data")
                    var item : Array<itemList> = gson.fromJson(jsonArray.toString(), Array<itemList>::class.java)
                    adapter!!.setItemList(item)
                    adapter!!.filter.filter(svItem!!.query)
                    srItem!!.isRefreshing=false
                    if(!item.isEmpty()){
                        Toast.makeText(this@itemActivity,"Data Berhasil Diambil", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@itemActivity,"Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error->
                    srItem!!.isRefreshing=false
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@itemActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(this@itemActivity,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
            queue!!.add(StringRequest)
        }else if(category == "Event"){
            srItem!!.isRefreshing=true
            val StringRequest: StringRequest = object : StringRequest(Method.GET,tempatWisataApi.GET_BY_NAMA_URL + kota,
                Response.Listener { response->
                    val gson = Gson()
                    val item: Array<itemList> = gson.fromJson(response,Array<itemList>::class.java)
                    adapter!!.setItemList(item)
                    adapter!!.filter.filter(svItem!!.query)
                    srItem!!.isRefreshing=false
                    if(!item.isEmpty()){
                        Toast.makeText(this@itemActivity,"Data Berhasil Diambil", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(this@itemActivity,"Data Kosong", Toast.LENGTH_SHORT).show()
                    }
                }, Response.ErrorListener { error->
                    srItem!!.isRefreshing=false
                    try{
                        val responseBody = String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@itemActivity,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    }catch (e: Exception){
                        Toast.makeText(this@itemActivity,e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            ){
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String,String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
            queue!!.add(StringRequest)
        }

    }

    fun setLoading(isLoading:Boolean){
        if(isLoading){
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        }else{
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }

    fun getBundle(){
        try{
            mbunlde = intent?.getBundleExtra("profile")!!
            if(mbunlde != null){
                vuser = mbunlde.getString("username")!!
                vpass = mbunlde.getString("password")!!
                vcity = mbunlde.getString("city")!!
                vcategory = mbunlde.getString("category")!!
            }else{

            }
        }catch (e: NullPointerException){
            vuser = ""
            vpass = ""
            vcity= ""
            vcategory = ""
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = MenuInflater(this)
        menuInflater.inflate(R.menu.menu ,menu)
        return true
    }


//    override fun onBackPressed() {
//        if(drawer.isDrawerOpen(GravityCompat.START)){
//            drawer.closeDrawer(GravityCompat.START)
//        }else{
//            super.onBackPressed()
//        }
//    }

    override fun onBackPressed() {
        val intent = Intent(this,pariwisata::class.java)
        val mBundle = Bundle()
        mBundle.putString("username",vuser)
        mBundle.putString("password",vpass)
        mBundle.putString("city",vcity)
        intent.putExtra("profile",mBundle)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menulogout) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this@itemActivity)
            builder.setMessage("Want to log out?")
                .setNegativeButton("No", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {

                    }
                })
                .setPositiveButton("YES", object : DialogInterface.OnClickListener {
                    override fun onClick(dialogInterface: DialogInterface, i: Int) {
                        startActivity(Intent(this@itemActivity, MainActivity::class.java))
                    }
                })
                .show()
        }
        else if(item.itemId == R.id.menuKota) {
            val intent = Intent(this, kota::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",vuser)
            mBundle.putString("password",vpass)
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }else if(item.itemId == R.id.menuGambar) {
            val intent = Intent(this, LibGlide::class.java)
            startActivity(intent)
        }else if(item.itemId == R.id.menuProfile){
            val intent = Intent(this, profile::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",vuser)
            mBundle.putString("password",vpass)
            mBundle.putString("city",vcity)
            mBundle.putString("category",vcategory)
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }else if(item.itemId == R.id.menuAddWisata){
            val intent = Intent(this, addWisata::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",vuser)
            mBundle.putString("password",vpass)
            mBundle.putString("city",vcity)
            mBundle.putString("category",vcategory)
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }else if(item.itemId == R.id.menuTiket){
            val intent = Intent(this, addTiket::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",vuser)
            mBundle.putString("password",vpass)
            mBundle.putString("city",vcity)
            mBundle.putString("category",vcategory)
            intent.putExtra("profile",mBundle)
            startActivity(intent)
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}