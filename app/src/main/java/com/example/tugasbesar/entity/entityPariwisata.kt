package com.example.tugasbesar.entity

import com.example.tugasbesar.R

class entityPariwisata(var nama : Int, var id :String) {
    companion object{
        @JvmField
        var listnamaPariwisata = arrayOf(
            entityPariwisata(R.drawable.akomodasilogo,"Akomodasi"),
            entityPariwisata(R.drawable.culinary_logo,"Kuliner"),
            entityPariwisata(R.drawable.wisatabig,"Wisata"),
            entityPariwisata(R.drawable.eventbig,"Event"),
        )
    }
}