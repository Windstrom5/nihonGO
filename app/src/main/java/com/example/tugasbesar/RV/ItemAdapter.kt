package com.example.tugasbesar.RV

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.tugasbesar.DetailsActivity
import com.example.tugasbesar.R
import com.example.tugasbesar.api.tempatWisataApi
import com.example.tugasbesar.editItem
import com.example.tugasbesar.entity.itemList
import com.example.tugasbesar.models.TempatWisata
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.util.*

class ItemAdapter(private var itemList: List<itemList>, context: Context) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>(), Filterable {
    private var filteredItemList: MutableList<itemList>
    private val context:Context
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var vcity : String
    lateinit var vcategory : String
    private var queue: RequestQueue? = null

    init {
        filteredItemList = ArrayList(itemList)
        this.context = context
        queue = Volley.newRequestQueue(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredItemList.size
    }

    fun setItemList(mahasiswaList: Array<itemList>){
        this.itemList = mahasiswaList.toList()
        filteredItemList = mahasiswaList.toMutableList()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = filteredItemList[position]
        holder.tvName.text = item.name
        holder.tvAlamat.text = item.alamat
        holder.tvRating.text = item.rating
        holder.tvPrice.text = "uploaded by "+item.user
        if(vuser != item.user){
            holder.btn_edit.setVisibility(View.GONE)
        }
        holder.btn_edit.setOnClickListener(View.OnClickListener {
            val popup = PopupMenu(context, holder.btn_edit)
            popup.inflate(R.menu.menu3)
            popup.show()

            popup.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener{
                override fun onMenuItemClick(menu: MenuItem): Boolean {
                    if (menu.itemId == R.id.menuDeleteItem) {
                        deleteItem(item.name)
                    }else if (menu.itemId == R.id.menuEditItem){
                        val intent = Intent(holder.btn_edit.context, editItem::class.java)
                        val mBundle = Bundle()
                        mBundle.putString("username",vuser)
                        mBundle.putString("password",vpass)
                        mBundle.putString("city", vcity)
                        mBundle.putString("nama",item.name)
                        mBundle.putString("user",item.user)
                        Log.d("Item",item.latitude)
                        mBundle.putString("alamat",item.alamat)
                        mBundle.putString("rating",item.rating)
                        mBundle.putString("price",item.price)
                        mBundle.putString("category",vcategory)
                        mBundle.putString("latitude",item.latitude)
                        mBundle.putString("longtitude",item.longtitude)
                        intent.putExtra("profile",mBundle)
                        holder.btnMore.context.startActivity(intent)
                    }
                    return true
                }
            })
        })

        holder.btnMore.setOnClickListener {
            val intent = Intent(holder.btnMore.context, DetailsActivity::class.java)
            val mBundle = Bundle()
            mBundle.putString("username",vuser)
            mBundle.putString("password",vpass)
            mBundle.putString("city", vcity)
            mBundle.putString("nama",item.name)
            mBundle.putString("user",item.user)
            Log.d("Item",item.latitude)
            mBundle.putString("alamat",item.alamat)
            mBundle.putString("rating",item.rating)
            mBundle.putString("price",item.price)
            mBundle.putString("category",vcategory)
            mBundle.putString("latitude",item.latitude)
            mBundle.putString("longtitude",item.longtitude)
            intent.putExtra("profile",mBundle)
            holder.btnMore.context.startActivity(intent)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charSequenceString = charSequence.toString()
                val filtered: MutableList<itemList> = java.util.ArrayList()
                if(charSequenceString.isEmpty()){
                    filtered.addAll(itemList)
                }else{
                    for(item in itemList){
                        if(item.name.lowercase(Locale.getDefault())
                                .contains(charSequenceString.lowercase(Locale.getDefault()))
                        )filtered.add(item)
                    }
                }
                val filterResults = FilterResults()
                filterResults.values = filtered
                for (i in filtered){
                    println(i.name)
                }
                return filterResults
            }
            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                filteredItemList.clear()
                filteredItemList.addAll(filterResults.values as List<itemList>)
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvName : TextView
        var tvAlamat : TextView
        var tvRating : TextView
        var tvPrice : TextView
        var btnMore : ImageButton
        var cvItem : CardView
        var btn_edit : ImageButton
        init {
            tvName = itemView.findViewById(R.id.tv_name)
            tvAlamat = itemView.findViewById(R.id.tv_alamat)
            tvRating = itemView.findViewById(R.id.tv_rating)
            tvPrice = itemView.findViewById(R.id.tv_price)
            btnMore = itemView.findViewById(R.id.btn_more)
            cvItem = itemView.findViewById(R.id.cv_item)
            btn_edit = itemView.findViewById(R.id.btn_edit)
        }
    }

    fun getVariable(user : String,pass : String, city : String,category : String){
        vuser = user
        vpass = pass
        vcity = city
        vcategory = category
    }

    private fun deleteItem(nama : String) {
        val stringRequest: StringRequest =
            object : StringRequest(Method.DELETE, tempatWisataApi.DELETE_URL + nama,
                Response.Listener { response ->
//                    val gson = Gson()
//                    val jsonObject = JSONObject(response)
//                    val jsonArray = jsonObject.getJSONObject("data")
//                    val experience = gson.fromJson(jsonArray.toString(), TempatWisata::class.java)
//                    if (experience != null)
                      Toast.makeText(context, "Success Delete", Toast.LENGTH_SHORT).show()
                },
                Response.ErrorListener { error ->
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            context,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): Map<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
        queue!!.add(stringRequest)
    }
}