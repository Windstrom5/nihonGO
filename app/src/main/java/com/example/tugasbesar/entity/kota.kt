package com.example.tugasbesar.entity

class kota(var nama : String) {
    companion object{
        @JvmField
        var listofnamaKota = arrayOf(
            kota("Nagoya"),
            kota("Tokyo"),
            kota("Akihabara"),
            kota("Kyoto"),
            kota("Sapporo"),
            kota("Hiroshima"),
            kota("Fukuoka"),
            kota("Yokohama"),
            kota("Nagasaki"),
            kota("Sendai")
        )
    }
}