package com.example.tugasbesar.entity

import com.example.tugasbesar.R

class entityPariwisata(var nama : Int, var id :String) {
    companion object{
        @JvmField
        var listnamaPariwisata = arrayOf(
            entityPariwisata(R.drawable.akomodasipbp2,"Akomodasi"),
            entityPariwisata(R.drawable.kulinerpbp,"Kuliner"),
            entityPariwisata(R.drawable.wisatapbp,"Wisata"),
            entityPariwisata(R.drawable.eventpbp,"Event"),
        )
    }
}