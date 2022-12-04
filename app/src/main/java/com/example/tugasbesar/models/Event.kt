package com.example.tugasbesar.models

class Event(var namaEvent : String, var alamat : String, var tgl : String,var price :String,
            var city: String,val latitude: Double, val longtitude: Double) {
    var id: Long? = null
}