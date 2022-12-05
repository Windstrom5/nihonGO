package com.example.tugasbesar.entity

import com.example.tugasbesar.R

class entityPariwisata(var nama : Int, var id :String) {
    companion object{
        @JvmField
        var listnamaPariwisata = arrayOf(
            entityPariwisata(R.drawable.akomodasibig,"Akomodasi"),
            entityPariwisata(R.drawable.kulinerbig,"Kuliner"),
            entityPariwisata(R.drawable.wisatabig,"Wisata"),
            entityPariwisata(R.drawable.eventbig,"Event"),
        )
    }
}