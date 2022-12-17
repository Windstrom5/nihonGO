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
import androidx.recyclerview.widget.RecyclerView
import com.example.tugasbesar.DetailsActivity
import com.example.tugasbesar.R
import com.example.tugasbesar.entity.itemList
import java.util.*

class ItemAdapter(private var itemList: List<itemList>, context: Context) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>(), Filterable {
    private var filteredItemList: MutableList<itemList>
    private val context:Context
    lateinit var vuser : String
    lateinit var vpass : String
    lateinit var vcity : String
    lateinit var vcategory : String

    init {
        filteredItemList = ArrayList(itemList)
        this.context = context
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
                    override fun onMenuItemClick(item: MenuItem): Boolean {
                        if (item.itemId == R.id.menuDeleteItem) {

                        }else if (item.itemId == R.id.menuEditItem){

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
}