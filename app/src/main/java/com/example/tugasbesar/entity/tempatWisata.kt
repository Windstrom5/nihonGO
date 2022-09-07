package com.example.tugasbesar.entity

class tempatWisata(var name : String, var alamat : String, var rating : Double) {
    companion object{
        @JvmField
        var listOftempatWisata = arrayOf(
            tempatWisata("Gunung Fuji", "jl.Fuji", 4.9),
            tempatWisata("Disney Land", "jl.Tokyo", 4.9)
        )
    }
}