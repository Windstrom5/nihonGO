package com.example.tugasbesar.entity

import com.example.tugasbesar.R

class kota(var nama : Int,var id :String) {
    companion object{
        @JvmField
        var listofnamaKota = arrayOf(
            kota(R.drawable.tokyo,"Tokyo")
        )
    }
}