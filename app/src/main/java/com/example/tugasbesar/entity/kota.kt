package com.example.tugasbesar.entity

import com.example.tugasbesar.R

class kota(var nama : Int,var id :String) {
    companion object{
        @JvmField
        var listofnamaKota = arrayOf(
            kota(R.drawable.tokyo,"Tokyo"),
            kota(R.drawable.kyoto,"Kyoto"),
            kota(R.drawable.kobe,"Kobe"),
            kota(R.drawable.hakone,"Hakone"),
            kota(R.drawable.akibahara,"Akihabara"),
            kota(R.drawable.hiroshima,"Hiroshima"),
            kota(R.drawable.yokohama,"Yokohama"),
            kota(R.drawable.osaka,"Osaka"),
            kota(R.drawable.sapporo,"Sapporo"),
            kota(R.drawable.nagasaki,"Nagasaki")
        )
    }
}