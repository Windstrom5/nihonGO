package com.example.tugasbesar.entity

import com.example.tugasbesar.R

class entityPariwisata(var nama : Int, var id :String) {
    companion object{
        @JvmField
        var listnamaPariwisata = arrayOf(
            entityPariwisata(R.drawable.tokyo,"Tokyo"),
            entityPariwisata(R.drawable.kyoto,"Kyoto"),
            entityPariwisata(R.drawable.kobe,"Kobe"),
            entityPariwisata(R.drawable.hakone,"Hakone"),
        )
    }
}