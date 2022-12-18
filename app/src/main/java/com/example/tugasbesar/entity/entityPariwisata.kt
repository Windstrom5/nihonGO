package com.example.tugasbesar.entity

import com.example.tugasbesar.R

class entityPariwisata(var nama : Int, var id :String) {
    companion object{
        @JvmField
        var listnamaPariwisata = arrayOf(
            entityPariwisata(R.drawable.txtaccomodation,"Akomodasi"),
            entityPariwisata(R.drawable.txtcullinary,"Kuliner"),
            entityPariwisata(R.drawable.txttourist,"Wisata"),
            entityPariwisata(R.drawable.txtcurrentevent,"Event"),
        )
    }
}